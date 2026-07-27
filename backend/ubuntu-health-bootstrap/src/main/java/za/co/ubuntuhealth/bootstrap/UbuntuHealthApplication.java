package za.co.ubuntuhealth.bootstrap;

import za.co.ubuntuhealth.bootstrap.config.UbuntuHealthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "za.co.ubuntuhealth")
@EnableConfigurationProperties(UbuntuHealthProperties.class)
public class UbuntuHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UbuntuHealthApplication.class, args);
    }
}
