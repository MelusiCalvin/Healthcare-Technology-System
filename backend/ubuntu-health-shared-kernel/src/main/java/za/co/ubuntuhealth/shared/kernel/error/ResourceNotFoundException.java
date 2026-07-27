package za.co.ubuntuhealth.shared.kernel.error;

public final class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
