package za.co.ubuntuhealth.bootstrap.web;

import za.co.ubuntuhealth.bootstrap.config.UbuntuHealthProperties;
import za.co.ubuntuhealth.bootstrap.web.filter.CorrelationIdFilter;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
class SystemStatusController {

    private final UbuntuHealthProperties properties;

    SystemStatusController(UbuntuHealthProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/status")
    ResponseEntity<ApiResponse<SystemStatusResponse>> status() {
        var response = new SystemStatusResponse(
                required(properties.getName()),
                required(properties.getEnvironment()),
                required(properties.getVersion()),
                "UP"
        );

        return ResponseEntity.ok(ApiResponse.success(response, CorrelationIdFilter.currentCorrelationId()));
    }

    private String required(@NotBlank String value) {
        return value;
    }
}
