package za.co.ubuntuhealth.bootstrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ubuntu-health")
public class UbuntuHealthProperties {
        private String name;
        private String environment;
        private String version;

        public UbuntuHealthProperties() {
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEnvironment() {
                return environment;
        }

        public void setEnvironment(String environment) {
                this.environment = environment;
        }

        public String getVersion() {
                return version;
        }

        public void setVersion(String version) {
                this.version = version;
        }
}
