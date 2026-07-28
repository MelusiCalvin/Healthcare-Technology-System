CREATE TABLE core.patient (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    date_of_birth date NOT NULL,
    identification_type varchar(64) NOT NULL,
    identification_number varchar(64) NOT NULL UNIQUE,
    preferred_language varchar(64) NOT NULL,
    province varchar(64) NOT NULL,
    phone_number varchar(32),
    email varchar(160),
    medical_aid_provider varchar(120),
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version bigint NOT NULL DEFAULT 0,
    CONSTRAINT patient_identification_type_check CHECK (identification_type IN (
        'SOUTH_AFRICAN_ID_NUMBER',
        'PASSPORT_NUMBER',
        'REFUGEE_PERMIT',
        'BIRTH_CERTIFICATE_NUMBER',
        'TEMPORARY_HEALTHCARE_NUMBER'
    )),
    CONSTRAINT patient_province_check CHECK (province IN (
        'EASTERN_CAPE',
        'FREE_STATE',
        'GAUTENG',
        'KWAZULU_NATAL',
        'LIMPOPO',
        'MPUMALANGA',
        'NORTHERN_CAPE',
        'NORTH_WEST',
        'WESTERN_CAPE'
    ))
);

CREATE TRIGGER patient_set_updated_at
    BEFORE UPDATE ON core.patient
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();
