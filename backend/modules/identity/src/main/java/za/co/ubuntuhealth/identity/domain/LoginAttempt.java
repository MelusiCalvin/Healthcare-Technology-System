package za.co.ubuntuhealth.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(schema = "iam", name = "login_attempt")
public class LoginAttempt {

    @Id
    @Column(name = "subject_hash")
    private String subjectHash;

    @Column(name = "failure_count", nullable = false)
    private int failureCount;

    @Column(name = "window_started_at", nullable = false)
    private Instant windowStartedAt;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    protected LoginAttempt() {
    }

    private LoginAttempt(String subjectHash, Instant now) {
        this.subjectHash = subjectHash;
        this.windowStartedAt = now;
    }

    public static LoginAttempt start(String subjectHash, Instant now) {
        return new LoginAttempt(subjectHash, now);
    }

    public boolean isLocked(Instant now) {
        return lockedUntil != null && lockedUntil.isAfter(now);
    }

    public void registerFailure(Instant now, int maxFailures, Instant windowEnd, Instant lockoutEnd) {
        if (windowStartedAt.isBefore(windowEnd)) {
            failureCount = 0;
            windowStartedAt = now;
            lockedUntil = null;
        }

        failureCount++;
        if (failureCount >= maxFailures) {
            lockedUntil = lockoutEnd;
        }
    }

    public void clearFailures(Instant now) {
        failureCount = 0;
        windowStartedAt = now;
        lockedUntil = null;
    }
}
