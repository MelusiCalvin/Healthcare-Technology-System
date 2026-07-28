package za.co.ubuntuhealth.identity.application;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import za.co.ubuntuhealth.identity.domain.PasswordCredential;
import za.co.ubuntuhealth.identity.domain.RefreshToken;
import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.infrastructure.persistence.PasswordCredentialRepository;
import za.co.ubuntuhealth.identity.infrastructure.persistence.RefreshTokenRepository;
import za.co.ubuntuhealth.identity.infrastructure.persistence.UserAccountRepository;
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

    @PostConstruct
    void bootstrapDefaultAdmin() {
        if (userAccountRepository.count() == 0) {
            UserAccount admin = UserAccount.create("admin", "admin@ubuntuhealth.za", "System Administrator");
            userAccountRepository.save(admin);
            passwordCredentialRepository.save(PasswordCredential.forUser(admin, passwordEncoder.encode("ChangeMe123!")));
        }
    }

    public AuthenticationResponse authenticate(String usernameOrEmail, String password, String userAgentHash, String sourceIpHash) {
        UserAccount user = userAccountRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new DomainException(ErrorCode.ACCESS_DENIED, "Invalid credentials."));

        if (!user.mayAuthenticate()) {
            throw new DomainException(ErrorCode.ACCESS_DENIED, "User account is not active.");
        }

        PasswordCredential credential = passwordCredentialRepository.findById(user.id())
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

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public record AuthenticationResponse(String accessToken, String refreshToken) {
    }
}
