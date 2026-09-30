package za.co.ubuntuhealth.patient.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "patient", schema = "core")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID patientId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "identification_type", nullable = false, length = 64)
    private SouthAfricanIdType identificationType;

    @Column(name = "identification_number", nullable = false, unique = true, length = 64)
    private String identificationNumber;

    @Column(name = "preferred_language", nullable = false, length = 64)
    private String preferredLanguage;

    @Column(nullable = false, length = 64)
    private String province;

    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    @Column(length = 160)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "medical_aid_provider", length = 120)
    private MedicalAidProvider medicalAidProvider;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Version
    private long version;

    protected Patient() {}

    public static Patient register(
            String firstName,
            String lastName,
            String email,
            LocalDate dateOfBirth,
            String phoneNumber,
            String identificationNumber,
            SouthAfricanIdType identificationType,
            MedicalAidProvider medicalAidProvider,
            String province,
            String preferredLanguage
    ) {
        Patient patient = new Patient();
        patient.firstName = firstName;
        patient.lastName = lastName;
        patient.email = email;
        patient.dateOfBirth = dateOfBirth;
        patient.phoneNumber = phoneNumber;
        patient.identificationNumber = identificationNumber;
        patient.identificationType = identificationType;
        patient.medicalAidProvider = medicalAidProvider;
        patient.province = province;
        patient.preferredLanguage = preferredLanguage;
        return patient;
    }

    public void update(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            SouthAfricanIdType identificationType,
            String identificationNumber,
            String phoneNumber,
            String email,
            MedicalAidProvider medicalAidProvider,
            String province,
            String preferredLanguage
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.medicalAidProvider = medicalAidProvider;
        this.province = province;
        this.preferredLanguage = preferredLanguage;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public SouthAfricanIdType getIdentificationType() {
        return identificationType;
    }

    public MedicalAidProvider getMedicalAidProvider() {
        return medicalAidProvider;
    }

    public String getProvince() {
        return province;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
