package za.co.ubuntuhealth.patient.web.dto;

import java.time.LocalDate;
import java.util.UUID;

import za.co.ubuntuhealth.patient.domain.MedicalAidProvider;
import za.co.ubuntuhealth.patient.domain.Patient;
import za.co.ubuntuhealth.patient.domain.SouthAfricanIdType;

public class PatientResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private SouthAfricanIdType identificationType;
    private String identificationNumber;
    private String phoneNumber;
    private String email;
    private MedicalAidProvider medicalAidProvider;

    public PatientResponse(Patient patient) {
        this.id = patient.getUserAccountId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.identificationType = patient.getIdentificationType();
        this.dateOfBirth = patient.getDateOfBirth();
        this.identificationNumber = patient.getIdentificationNumber();
        this.phoneNumber = patient.getPhoneNumber();
        this.email = patient.getEmail();
        this.medicalAidProvider = patient.getMedicalAidProvider();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public MedicalAidProvider getMedicalAidProvider() {
        return medicalAidProvider;
    }
    // public static void main(String[] args) {
        
    // }
}
