package za.co.ubuntuhealth.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import za.co.ubuntuhealth.bootstrap.config.UbuntuHealthProperties;

@SpringBootApplication
@EnableConfigurationProperties(UbuntuHealthProperties.class)
public class UbuntuHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UbuntuHealthApplication.class, args);
    }
}
