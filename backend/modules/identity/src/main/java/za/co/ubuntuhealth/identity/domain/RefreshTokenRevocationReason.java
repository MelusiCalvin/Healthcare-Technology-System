package za.co.ubuntuhealth.identity.domain;

public enum RefreshTokenRevocationReason {
    ROTATED,
    LOGOUT,
    REUSE_DETECTED,
    USER_DISABLED,
    SECURITY_EVENT
}
