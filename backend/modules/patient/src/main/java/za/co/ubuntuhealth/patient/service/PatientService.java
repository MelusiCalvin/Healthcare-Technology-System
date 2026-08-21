package za.co.ubuntuhealth.patient.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.ubuntuhealth.patient.api.PatientCreateRequest;
import za.co.ubuntuhealth.patient.api.PatientResponse;
import za.co.ubuntuhealth.patient.domain.Patient;
import za.co.ubuntuhealth.patient.repository.PatientRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientResponse create(PatientCreateRequest request) {
        String patientNumber = generatePatientNumber();
        Patient patient = new Patient(
            request.firstName().trim(),
            request.lastName().trim(),
            UUID.randomUUID(),
            patientNumber,
            request.sex().trim(),
            normalize(request.email()),
            normalize(request.phoneNumber()),
            request.dateOfBirth(),
            UUID.randomUUID().toString()
        );

        return PatientResponse.from(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(UUID id) {
        return patientRepository.findById(id)
                .map(PatientResponse::from)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PatientResponse findByPatientNumber(String patientNumber) {
        return patientRepository.findByPatientNumber(patientNumber)
                .map(PatientResponse::from)
                .orElseThrow(() -> new PatientNotFoundException(patientNumber));
    }

    private String generatePatientNumber() {
        String patientNumber;
        do {
            patientNumber = "UH-" + UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 10)
                    .toUpperCase();
        } while (patientRepository.existsByPatientNumber(patientNumber));
        return patientNumber;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
