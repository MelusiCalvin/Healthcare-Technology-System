package za.co.ubuntuhealth.bootstrap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfiguration {

    @Bean
    OpenAPI ubuntuHealthOpenApi(UbuntuHealthProperties properties) {
        return new OpenAPI()
                .info(new Info()
                        .title(String.format("%s API", properties.getName()))
                        .version(properties.getVersion())
                        .description("Secure healthcare platform API. Clinical decisions remain the responsibility of authorised healthcare professionals.")
                        .license(new License().name("Proprietary")))
                .addServersItem(new Server().url("/").description("Current environment"));
    }
}
