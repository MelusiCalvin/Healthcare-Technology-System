package za.co.ubuntuhealth.patient.web.dto;

import java.time.LocalDate;
import java.util.UUID;

import za.co.ubuntuhealth.patient.domain.Patient;
import za.co.ubuntuhealth.patient.domain.SouthAfricanIdType;

public class PatientResponse {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final SouthAfricanIdType identificationType;
    private final String identificationNumber;
    private final String preferredLanguage;
    private final String province;
    private final String phoneNumber;
    private final String email;
    private final String medicalAidProvider;

    public PatientResponse(Patient patient) {
        this.id = patient.getPatientId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.dateOfBirth = patient.getDateOfBirth();
        this.identificationType = patient.getIdentificationType();
        this.identificationNumber = null;
        this.preferredLanguage = null;
        this.province = null;
        this.phoneNumber = patient.getPhoneNumber();
        this.email = patient.getEmail();
        this.medicalAidProvider = null;
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
