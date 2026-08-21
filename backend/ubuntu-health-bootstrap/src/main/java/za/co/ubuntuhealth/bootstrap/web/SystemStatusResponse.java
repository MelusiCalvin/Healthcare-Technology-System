package za.co.ubuntuhealth.bootstrap.web;

public class SystemStatusResponse {
    private final String service;
    private final String environment;
    private final String version;
    private final String status;

    public SystemStatusResponse(String service, String environment, String version, String status) {
        this.service = service;
        this.environment = environment;
        this.version = version;
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }
}
