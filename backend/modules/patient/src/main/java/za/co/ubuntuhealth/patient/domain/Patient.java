package za.co.ubuntuhealth.patient.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "patient", schema = "patient")
public class Patient {

    @Id
    private UUID id;

    @Column(name = "patient_number", nullable = false, unique = true, length = 32)
    private String patientNumber;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "sex", nullable = false, length = 20)
    private String sex;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "email", length = 254)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Patient() {
    }

    public Patient(UUID id, String patientNumber, String firstName, String lastName,
                   LocalDate dateOfBirth, String sex, String phoneNumber, String email) {
        this.id = id;
        this.patientNumber = patientNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.email = email;
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public UUID getId() { return id; }
    public String getPatientNumber() { return patientNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getSex() { return sex; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void updateContactDetails(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.updatedAt = OffsetDateTime.now();
    }
}
