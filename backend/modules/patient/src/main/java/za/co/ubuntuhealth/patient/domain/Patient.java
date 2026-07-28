package za.co.ubuntuhealth.patient.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "core", name = "patient")
public class Patient {

    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "identification_type", nullable = false)
    private SouthAfricanIdType identificationType;

    @Column(name = "identification_number", nullable = false, unique = true)
    private String identificationNumber;

    @Column(name = "preferred_language", nullable = false)
    private String preferredLanguage;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "medical_aid_provider")
    private String medicalAidProvider;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Version
    private long version;

    protected Patient() {
    }

    private Patient(
            UUID id,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            SouthAfricanIdType identificationType,
            String identificationNumber,
            String preferredLanguage,
            String province,
            String phoneNumber,
            String email,
            String medicalAidProvider
    ) {
        this.id = id;
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");
        this.identificationType = Objects.requireNonNull(identificationType, "identificationType must not be null");
        this.identificationNumber = Objects.requireNonNull(identificationNumber, "identificationNumber must not be null");
        this.preferredLanguage = Objects.requireNonNull(preferredLanguage, "preferredLanguage must not be null");
        this.province = Objects.requireNonNull(province, "province must not be null");
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.medicalAidProvider = medicalAidProvider;
    }

    public static Patient register(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            SouthAfricanIdType identificationType,
            String identificationNumber,
            String preferredLanguage,
            String province,
            String phoneNumber,
            String email,
            String medicalAidProvider
    ) {
        validateIdentificationNumber(identificationType, identificationNumber);
        return new Patient(
                UUID.randomUUID(),
                firstName,
                lastName,
                dateOfBirth,
                identificationType,
                identificationNumber,
                preferredLanguage,
                province,
                phoneNumber,
                email,
                medicalAidProvider
        );
    }

    private static void validateIdentificationNumber(SouthAfricanIdType type, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Identification number is required.");
        }

        switch (type) {
            case SOUTH_AFRICAN_ID_NUMBER -> validateSouthAfricanId(value);
            case PASSPORT_NUMBER, REFUGEE_PERMIT, BIRTH_CERTIFICATE_NUMBER, TEMPORARY_HEALTHCARE_NUMBER -> {
                if (value.length() < 6 || value.length() > 32) {
                    throw new IllegalArgumentException("Identification number must be between 6 and 32 characters.");
                }
            }
        }
    }

    private static void validateSouthAfricanId(String idNumber) {
        if (!idNumber.matches("\\d{13}")) {
            throw new IllegalArgumentException("South African ID number must contain 13 digits.");
        }

        int sum = 0;
        for (int i = 0; i < idNumber.length(); i++) {
            int digit = Character.digit(idNumber.charAt(i), 10);
            if (i % 2 == 0) {
                sum += digit;
            } else {
                int doubleDigit = digit * 2;
                sum += doubleDigit / 10 + doubleDigit % 10;
            }
        }

        if (sum % 10 != 0) {
            throw new IllegalArgumentException("South African ID number is invalid.");
        }
    }

    public UUID id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public LocalDate dateOfBirth() {
        return dateOfBirth;
    }

    public SouthAfricanIdType identificationType() {
        return identificationType;
    }

    public String identificationNumber() {
        return identificationNumber;
    }

    public String preferredLanguage() {
        return preferredLanguage;
    }

    public String province() {
        return province;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public String email() {
        return email;
    }

    public String medicalAidProvider() {
        return medicalAidProvider;
    }

    public void update(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            SouthAfricanIdType identificationType,
            String identificationNumber,
            String preferredLanguage,
            String province,
            String phoneNumber,
            String email,
            String medicalAidProvider
    ) {
        validateIdentificationNumber(identificationType, identificationNumber);
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");
        this.identificationType = Objects.requireNonNull(identificationType, "identificationType must not be null");
        this.identificationNumber = Objects.requireNonNull(identificationNumber, "identificationNumber must not be null");
        this.preferredLanguage = Objects.requireNonNull(preferredLanguage, "preferredLanguage must not be null");
        this.province = Objects.requireNonNull(province, "province must not be null");
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.medicalAidProvider = medicalAidProvider;
    }
}
