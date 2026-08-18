CREATE TABLE patient.patient (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_number varchar(32) NOT NULL UNIQUE,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    date_of_birth date NOT NULL,
    sex varchar(20) NOT NULL,
    phone_number varchar(30),
    email varchar(254),
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_patient_name
    ON patient.patient (last_name, first_name);

CREATE INDEX idx_patient_date_of_birth
    ON patient.patient (date_of_birth);

CREATE TRIGGER patient_set_updated_at
    BEFORE UPDATE ON patient.patient
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

COMMENT ON TABLE patient.patient IS 'Core patient demographic record. Sensitive identifiers should be stored separately and protected by application authorization.';
