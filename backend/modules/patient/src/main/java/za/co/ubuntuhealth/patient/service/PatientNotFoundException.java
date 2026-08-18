package za.co.ubuntuhealth.patient.service;

import java.util.UUID;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(UUID id) {
        super("Patient not found: " + id);
    }

    public PatientNotFoundException(String patientNumber) {
        super("Patient not found: " + patientNumber);
    }
}
