package za.co.ubuntuhealth.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ubuntuhealth.patient.domain.Patient;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByPatientNumber(String patientNumber);

    boolean existsByPatientNumber(String patientNumber);
}
