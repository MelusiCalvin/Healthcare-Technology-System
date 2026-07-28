package za.co.ubuntuhealth.patient.web.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import za.co.ubuntuhealth.patient.domain.SouthAfricanIdType;

public class PatientRegistrationRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Identification type is required")
    private SouthAfricanIdType identificationType;

    @NotBlank(message = "Identification number is required")
    private String identificationNumber;

    @NotBlank(message = "Preferred language is required")
    private String preferredLanguage;

    @NotBlank(message = "Province is required")
    private String province;

    @Pattern(regexp = "^$|^\\+?[0-9]{7,15}$", message = "Phone number must be a valid international or local number")
    private String phoneNumber;

    @Email(message = "Email must be valid")
    private String email;

    private String medicalAidProvider;

    public PatientRegistrationRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public SouthAfricanIdType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(SouthAfricanIdType identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedicalAidProvider() {
        return medicalAidProvider;
    }

    public void setMedicalAidProvider(String medicalAidProvider) {
        this.medicalAidProvider = medicalAidProvider;
    }
}
