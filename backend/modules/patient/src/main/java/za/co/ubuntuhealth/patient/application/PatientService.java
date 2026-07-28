package za.co.ubuntuhealth.patient.application;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import za.co.ubuntuhealth.patient.domain.Patient;
import za.co.ubuntuhealth.patient.domain.SouthAfricanIdType;
import za.co.ubuntuhealth.patient.infrastructure.PatientRepository;
import za.co.ubuntuhealth.shared.kernel.error.DomainException;
import za.co.ubuntuhealth.shared.kernel.error.ErrorCode;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(PatientRegistrationCommand command) {
        if (patientRepository.findByIdentificationNumber(command.identificationNumber()).isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, "A patient with this identification number already exists.");
        }

        Patient patient = Patient.register(
                command.firstName(),
                command.lastName(),
                command.dateOfBirth(),
                command.identificationType(),
                command.identificationNumber(),
                command.preferredLanguage(),
                command.province(),
                command.phoneNumber(),
                command.email(),
                command.medicalAidProvider()
        );

        return patientRepository.save(patient);
    }

    public Patient getPatient(UUID patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new DomainException(ErrorCode.RESOURCE_NOT_FOUND, "Patient not found."));
    }

    public Page<Patient> searchPatients(String query, Pageable pageable) {
        return patientRepository.search(query == null || query.isBlank() ? "" : query, pageable);
    }

    public Patient updatePatient(UUID patientId, PatientUpdateCommand command) {
        Patient patient = getPatient(patientId);

        if (!patient.identificationNumber().equals(command.identificationNumber())
                && patientRepository.findByIdentificationNumber(command.identificationNumber()).isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, "A patient with this identification number already exists.");
        }

        patient.update(
                command.firstName(),
                command.lastName(),
                command.dateOfBirth(),
                command.identificationType(),
                command.identificationNumber(),
                command.preferredLanguage(),
                command.province(),
                command.phoneNumber(),
                command.email(),
                command.medicalAidProvider()
        );

        return patientRepository.save(patient);
    }

    public void deletePatient(UUID patientId) {
        Patient patient = getPatient(patientId);
        patientRepository.delete(patient);
    }

    public Page<Patient> searchPatients(String query, String province, String language, Pageable pageable) {
        String normalizedQuery = query == null || query.isBlank() ? "" : query;
        return patientRepository.search(normalizedQuery, province, language, pageable);
    }

    public record PatientRegistrationCommand(
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
    }

        public record PatientUpdateCommand(
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
        }
}
