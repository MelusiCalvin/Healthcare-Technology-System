package za.co.ubuntuhealth.patient.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.domain.UserRole;

@Entity
@Table(name = "patients")
public class Patient extends UserAccount {

    @Column(name = "user_account_id", nullable = false, unique = true)
    private UUID userAccountId;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID patientId;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    private String phoneNumber;

    protected Patient() {
        super();
    }

    public Patient(
            String firstName,
            String lastName,
            UUID userAccountId,
            String patientNumber,
            String sex,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String passwordHash
    ) {
        super(firstName, lastName, patientNumber, email, passwordHash, Set.of(UserRole.PATIENT));
        this.userAccountId = userAccountId;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public UUID getUserAccountId() {
        return userAccountId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public OffsetDateTime getUpdatedAt() {
        return super.getCreatedAt();
    }
}
