package za.co.ubuntuhealth.identity.application;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.ubuntuhealth.identity.domain.PasswordCredential;
import za.co.ubuntuhealth.identity.domain.RefreshToken;
import za.co.ubuntuhealth.identity.domain.RefreshTokenRevocationReason;
import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.domain.UserRole;
import za.co.ubuntuhealth.identity.infrastructure.persistence.PasswordCredentialRepository;
import za.co.ubuntuhealth.identity.infrastructure.persistence.RefreshTokenRepository;
import za.co.ubuntuhealth.identity.infrastructure.persistence.UserAccountRepository;
import za.co.ubuntuhealth.identity.web.dto.RegistrationRequest;
import za.co.ubuntuhealth.shared.kernel.error.DomainException;
import za.co.ubuntuhealth.shared.kernel.error.ErrorCode;

@Service
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenFactory tokenFactory;
    private final PasswordEncoder passwordEncoder;
    private final Duration refreshTokenTtl;

    public AuthenticationService(
            UserAccountRepository userAccountRepository,
            PasswordCredentialRepository passwordCredentialRepository,
            RefreshTokenRepository refreshTokenRepository,
            TokenFactory tokenFactory,
            AuthenticationProperties properties
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordCredentialRepository = passwordCredentialRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenFactory = tokenFactory;
        this.refreshTokenTtl = properties.refreshTokenTtl();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public AuthenticationResponse register(RegistrationRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();
        if (userAccountRepository.existsByUsernameIgnoreCase(username)) {
            throw new DomainException(ErrorCode.CONFLICT, "That username is already in use.");
        }
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new DomainException(ErrorCode.CONFLICT, "That email address is already registered.");
        }

        UserAccount user = new UserAccount(
                request.getFirstName().trim(),
                request.getLastName().trim(),
                username,
                email,
                passwordEncoder.encode(request.getPassword()),
                Set.of(UserRole.PATIENT)
        );
        userAccountRepository.save(user);
        passwordCredentialRepository.save(PasswordCredential.forUser(user, user.getPasswordHash()));
        return issueTokens(user, "self-registration", "unknown");
    }

    @Transactional
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

        return issueTokens(user, userAgentHash, sourceIpHash);
    }

    private AuthenticationResponse issueTokens(UserAccount user, String userAgentHash, String sourceIpHash) {
        String accessToken = tokenFactory.createAccessToken(new UserAccountPrincipal(user));
        String refreshToken = tokenFactory.createRefreshToken();
        String refreshTokenHash = tokenFactory.hashToken(refreshToken);
        RefreshToken refreshTokenEntity = RefreshToken.issue(user, refreshTokenHash, UUID.randomUUID(), Instant.now().plus(refreshTokenTtl), userAgentHash, sourceIpHash);
        refreshTokenRepository.save(refreshTokenEntity);

        user.recordSuccessfulLogin(Instant.now());
        userAccountRepository.save(user);

        return new AuthenticationResponse(accessToken, refreshToken, user);
    }

    @Transactional
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenRepository.findByTokenHash(tokenFactory.hashToken(refreshToken))
                .ifPresent(token -> {
                    token.revoke(RefreshTokenRevocationReason.LOGOUT, Instant.now());
                    refreshTokenRepository.save(token);
                });
    }

    public record AuthenticationResponse(String accessToken, String refreshToken, UserAccount user) {
    }
}
