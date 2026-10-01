package za.co.ubuntuhealth.identity.application;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import za.co.ubuntuhealth.identity.api.AuthResponse;
import za.co.ubuntuhealth.identity.api.LoginRequest;
import za.co.ubuntuhealth.identity.api.RegisterRequest;
import za.co.ubuntuhealth.identity.domain.PasswordCredential;
import za.co.ubuntuhealth.identity.domain.RefreshToken;
import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.domain.UserRole;
import za.co.ubuntuhealth.identity.infrastructure.persistence.PasswordCredentialRepository;
import za.co.ubuntuhealth.identity.infrastructure.persistence.RefreshTokenRepository;
import za.co.ubuntuhealth.identity.infrastructure.persistence.UserAccountRepository;
import za.co.ubuntuhealth.identity.service.InvalidCredentialsException;
import za.co.ubuntuhealth.identity.service.UsernameAlreadyExistsException;
import za.co.ubuntuhealth.shared.kernel.error.DomainException;
import za.co.ubuntuhealth.shared.kernel.error.ErrorCode;

@Service
@Transactional
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TokenFactory tokenFactory;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProperties properties;
    private final Duration refreshTokenTtl;

    public AuthenticationService(
            UserAccountRepository userAccountRepository,
            PasswordCredentialRepository passwordCredentialRepository,
            RefreshTokenRepository refreshTokenRepository,
            JdbcTemplate jdbcTemplate,
            TokenFactory tokenFactory,
            AuthenticationProperties properties,
            PasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordCredentialRepository = passwordCredentialRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.tokenFactory = tokenFactory;
        this.properties = properties;
        this.refreshTokenTtl = properties.refreshTokenTtl();
        this.passwordEncoder = passwordEncoder;
    }
    


    public AuthResponse register(RegisterRequest request) {
        String username = request.username().trim();
        if (userAccountRepository.existsByUsernameIgnoreCase(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        UserAccount user = new UserAccount();
        return AuthResponse.from(userAccountRepository.save(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest request) {
        UserAccount user = userAccountRepository.findByUsernameOrEmail(request.username().trim())
                .orElseThrow(InvalidCredentialsException::new);
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return AuthResponse.from(user);
    }

    @EventListener(ApplicationReadyEvent.class)
    void bootstrapDefaultAdmin() {
        AuthenticationProperties.BootstrapAdmin config = properties.bootstrapAdmin();
        if (!config.isEnabled()) {
            return;
        }

        String username = config.getUsername() == null ? "" : config.getUsername().trim();
        UserAccount admin = userAccountRepository.findByUsernameOrEmail(username).orElse(null);
        String[] nameParts = config.getDisplayName() == null
                ? new String[0]
                : config.getDisplayName().trim().split("\\s+", 2);
        if (admin == null && userAccountRepository.count() != 0) {
            return;
        }
        if (admin == null && (nameParts.length != 2
                || username.isBlank()
                || config.getEmail() == null || config.getEmail().isBlank()
                || config.getPassword() == null || config.getPassword().isBlank())) {
            throw new IllegalStateException("Bootstrap admin requires username, email, password, and a first and last name.");
        }

        if (admin == null) {
            admin = userAccountRepository.save(new UserAccount(
                    nameParts[0],
                    nameParts[1],
                    username,
                    config.getEmail().trim(),
                    passwordEncoder.encode(config.getPassword()),
                    Set.of(UserRole.SYSTEM_ADMIN)
            ));
        }

        jdbcTemplate.update("""
                INSERT INTO iam.user_account (id, username, email, display_name, status)
                VALUES (?, ?, ?, ?, 'ACTIVE')
                ON CONFLICT (id) DO NOTHING
                """, admin.getId(), admin.getUsername(), admin.getEmail(),
                admin.getFirstName() + " " + admin.getLastName());

        if (!passwordCredentialRepository.existsById(admin.getId())) {
            passwordCredentialRepository.save(PasswordCredential.forUser(admin, admin.getPasswordHash()));
        }
    }

    public AuthenticationResponse authenticate(String usernameOrEmail, String password, String userAgentHash, String sourceIpHash) {
        UserAccount user = userAccountRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new DomainException(ErrorCode.ACCESS_DENIED, "Invalid credentials."));

        if (!user.isActive()) {
            throw new DomainException(ErrorCode.ACCESS_DENIED, "User account is not active.");
        }

        PasswordCredential credential = passwordCredentialRepository.findById(user.getId())
                .orElseThrow(() -> new DomainException(ErrorCode.ACCESS_DENIED, "Invalid credentials."));

        if (!passwordEncoder.matches(password, credential.passwordHash())) {
            throw new DomainException(ErrorCode.ACCESS_DENIED, "Invalid credentials.");
        }

        String accessToken = tokenFactory.createAccessToken(new UserAccountPrincipal(user));
        String refreshToken = tokenFactory.createRefreshToken();
        String refreshTokenHash = tokenFactory.hashToken(refreshToken);
        RefreshToken refreshTokenEntity = RefreshToken.issue(user, refreshTokenHash, UUID.randomUUID(), Instant.now().plus(refreshTokenTtl), userAgentHash, sourceIpHash);
        refreshTokenRepository.save(refreshTokenEntity);

        user.recordSuccessfulLogin(Instant.now());
        userAccountRepository.save(user);

        return new AuthenticationResponse(
            accessToken,
            refreshToken,
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRoles()
        );
    }

        public record AuthenticationResponse(
            String accessToken,
            String refreshToken,
            UUID userId,
            String username,
            String firstName,
            String lastName,
            String email,
            Set<UserRole> roles
        ) {
    }
}