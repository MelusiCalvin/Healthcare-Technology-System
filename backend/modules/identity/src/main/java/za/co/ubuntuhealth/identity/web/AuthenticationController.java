package za.co.ubuntuhealth.identity.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import za.co.ubuntuhealth.identity.application.AuthenticationService;
import za.co.ubuntuhealth.identity.web.dto.AuthenticationRequest;
import za.co.ubuntuhealth.identity.web.dto.AuthenticationResponse;
import za.co.ubuntuhealth.identity.web.dto.LogoutRequest;
import za.co.ubuntuhealth.identity.web.dto.RegistrationRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletRequest servletRequest
    ) {
        String userAgent = servletRequest.getHeader("User-Agent");
        String userAgentHash = userAgent != null ? Integer.toHexString(userAgent.hashCode()) : "unknown";
        String sourceIp = servletRequest.getRemoteAddr();

        var authenticationResult = authenticationService.authenticate(
                request.getUsernameOrEmail(),
                request.getPassword(),
                userAgentHash,
                sourceIp
        );

        return ResponseEntity.ok(new AuthenticationResponse(
                authenticationResult.accessToken(),
                authenticationResult.refreshToken(),
                authenticationResult.user()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.revokeRefreshToken(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        var result = authenticationService.register(request);
        return ResponseEntity.ok(new AuthenticationResponse(
                result.accessToken(),
                result.refreshToken(),
                result.user()
        ));
    }
}
