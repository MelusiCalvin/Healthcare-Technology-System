package za.co.ubuntuhealth.identity.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 120) String username,
        @NotBlank @Size(min = 8, max = 72) String password
) {}
