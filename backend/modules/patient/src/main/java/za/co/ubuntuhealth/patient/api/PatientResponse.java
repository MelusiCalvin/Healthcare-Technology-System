package za.co.ubuntuhealth.patient.api;

import za.co.ubuntuhealth.patient.domain.Patient;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PatientResponse(
        UUID userAccountId,
        UUID patientId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String sex,
        String phoneNumber,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getUserAccountId(), patient.getPatientId(), patient.getFirstName(),
                patient.getLastName(), patient.getDateOfBirth(), patient.getSex(),
                patient.getPhoneNumber(), patient.getEmail(), patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
