package za.co.ubuntuhealth.patient.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class PatientDtos {

    private PatientDtos() {
    }

    public record CreatePatientRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotNull LocalDate dateOfBirth,
            @NotBlank String sex,
            String phoneNumber,
            @Email String email
    ) {
    }

    public record UpdatePatientContactRequest(
            String phoneNumber,
            @Email String email
    ) {
    }

    public record PatientResponse(
            UUID id,
            String patientNumber,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String sex,
            String phoneNumber,
            String email,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
    }
}
