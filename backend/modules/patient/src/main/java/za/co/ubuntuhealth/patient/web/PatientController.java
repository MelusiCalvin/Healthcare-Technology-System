package za.co.ubuntuhealth.patient.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import za.co.ubuntuhealth.patient.application.PatientService;
import za.co.ubuntuhealth.patient.application.PatientService.PatientRegistrationCommand;
import za.co.ubuntuhealth.patient.web.dto.PatientRegistrationRequest;
import za.co.ubuntuhealth.patient.web.dto.PatientResponse;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRegistrationRequest request) {
        PatientRegistrationCommand command = new PatientRegistrationCommand(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getIdentificationType(),
                request.getIdentificationNumber(),
                request.getPreferredLanguage(),
                request.getProvince(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getMedicalAidProvider()
        );

        var patient = patientService.registerPatient(command);
        return ResponseEntity.ok(new PatientResponse(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable UUID id) {
        var patient = patientService.getPatient(id);
        return ResponseEntity.ok(new PatientResponse(patient));
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> searchPatients(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "lastName") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<PatientResponse> patients = patientService.searchPatients(query, province, language, pageable).map(PatientResponse::new);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLINICIAN') or hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable UUID id, @Valid @RequestBody za.co.ubuntuhealth.patient.web.dto.PatientUpdateRequest request) {
        var command = new PatientService.PatientUpdateCommand(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getIdentificationType(),
                request.getIdentificationNumber(),
                request.getPreferredLanguage(),
                request.getProvince(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getMedicalAidProvider()
        );

        var updated = patientService.updatePatient(id, command);
        return ResponseEntity.ok(new PatientResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
