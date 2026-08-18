package za.co.ubuntuhealth.identity.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.ubuntuhealth.identity.api.AuthResponse;
import za.co.ubuntuhealth.identity.api.LoginRequest;
import za.co.ubuntuhealth.identity.api.RegisterRequest;
import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.domain.UserRole;
import za.co.ubuntuhealth.identity.repository.UserAccountRepository;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AuthenticationService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        String username = request.username().trim();
        if (repository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        UserAccount user = new UserAccount(
                UUID.randomUUID(),
                username,
                passwordEncoder.encode(request.password()),
                Set.of(UserRole.PATIENT)
        );
        return AuthResponse.from(repository.save(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest request) {
        UserAccount user = repository.findByUsername(request.username().trim())
                .orElseThrow(InvalidCredentialsException::new);
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return AuthResponse.from(user);
    }
}
