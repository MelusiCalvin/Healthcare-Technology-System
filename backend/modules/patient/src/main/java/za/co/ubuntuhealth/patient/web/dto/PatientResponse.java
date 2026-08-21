package za.co.ubuntuhealth.patient.web.dto;

import java.time.LocalDate;
import java.util.UUID;

import za.co.ubuntuhealth.patient.domain.Patient;
import za.co.ubuntuhealth.patient.domain.SouthAfricanIdType;

public class PatientResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private SouthAfricanIdType identificationType;
    private String identificationNumber;
    private String preferredLanguage;
    private String province;
    private String phoneNumber;
    private String email;
    private String medicalAidProvider;

    public PatientResponse(Patient patient) {
        this.id = patient.getUserAccountId().getId();
        this.firstName = patient.firstName();
        this.lastName = patient.lastName();
        this.dateOfBirth = patient.dateOfBirth();
        this.identificationType = patient.identificationType();
        this.identificationNumber = patient.identificationNumber();
        this.preferredLanguage = patient.preferredLanguage();
        this.province = patient.province();
        this.phoneNumber = patient.phoneNumber();
        this.email = patient.email();
        this.medicalAidProvider = patient.medicalAidProvider();
    }

    public UUID getId() {
        return id;
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

    public SouthAfricanIdType getIdentificationType() {
        return identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public String getProvince() {
        return province;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getMedicalAidProvider() {
        return medicalAidProvider;
    }
    // public static void main(String[] args) {
        
    // }
}
