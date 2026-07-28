package za.co.ubuntuhealth.patient.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import za.co.ubuntuhealth.patient.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByIdentificationNumber(String identificationNumber);

    @Query("select p from Patient p where " +
            "(:province is null or p.province = :province) and " +
            "(:language is null or p.preferredLanguage = :language) and (" +
            "lower(p.firstName) like lower(concat('%', :query, '%')) " +
            "or lower(p.lastName) like lower(concat('%', :query, '%')) " +
            "or lower(p.identificationNumber) like lower(concat('%', :query, '%')))")
    Page<Patient> search(String query, String province, String language, Pageable pageable);
}
