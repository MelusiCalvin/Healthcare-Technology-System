package za.co.ubuntuhealth.bootstrap;

import za.co.ubuntuhealth.bootstrap.config.UbuntuHealthProperties;
import za.co.ubuntuhealth.identity.application.AuthenticationProperties;
import za.co.ubuntuhealth.identity.application.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "za.co.ubuntuhealth")
@EntityScan(basePackages = {
    "za.co.ubuntuhealth.identity.domain",
    "za.co.ubuntuhealth.patient.domain"
})
@EnableJpaRepositories(basePackages = {
    "za.co.ubuntuhealth.identity.infrastructure.persistence",
    "za.co.ubuntuhealth.patient.infrastructure"
})
@EnableConfigurationProperties({
    UbuntuHealthProperties.class,
    AuthenticationProperties.class,
    JwtProperties.class
})
public class UbuntuHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UbuntuHealthApplication.class, args);
    }
}
