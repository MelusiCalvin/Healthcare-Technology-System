CREATE TABLE reference.province (
    code varchar(3) PRIMARY KEY,
    name varchar(80) NOT NULL UNIQUE,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reference.language (
    code varchar(8) PRIMARY KEY,
    name varchar(80) NOT NULL UNIQUE,
    native_name varchar(80) NOT NULL,
    official_in_south_africa boolean NOT NULL DEFAULT false,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reference.identifier_type (
    code varchar(64) PRIMARY KEY,
    display_name varchar(120) NOT NULL UNIQUE,
    south_african_format boolean NOT NULL DEFAULT false,
    active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reference.code_system (
    code varchar(64) PRIMARY KEY,
    display_name varchar(120) NOT NULL UNIQUE,
    uri varchar(512) NOT NULL UNIQUE,
    active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE finance.payer (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code varchar(64) NOT NULL UNIQUE,
    legal_name varchar(180) NOT NULL,
    payer_type varchar(32) NOT NULL,
    active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT payer_type_check CHECK (payer_type IN ('MEDICAL_AID', 'NHI', 'SELF_PAY', 'OTHER'))
);

CREATE TRIGGER province_set_updated_at
    BEFORE UPDATE ON reference.province
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER language_set_updated_at
    BEFORE UPDATE ON reference.language
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER identifier_type_set_updated_at
    BEFORE UPDATE ON reference.identifier_type
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER code_system_set_updated_at
    BEFORE UPDATE ON reference.code_system
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER payer_set_updated_at
    BEFORE UPDATE ON finance.payer
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

INSERT INTO reference.province (code, name) VALUES
    ('EC', 'Eastern Cape'),
    ('FS', 'Free State'),
    ('GP', 'Gauteng'),
    ('KZN', 'KwaZulu-Natal'),
    ('LP', 'Limpopo'),
    ('MP', 'Mpumalanga'),
    ('NC', 'Northern Cape'),
    ('NW', 'North West'),
    ('WC', 'Western Cape');

INSERT INTO reference.language (code, name, native_name, official_in_south_africa) VALUES
    ('af', 'Afrikaans', 'Afrikaans', true),
    ('en', 'English', 'English', true),
    ('nr', 'isiNdebele', 'isiNdebele', true),
    ('xh', 'isiXhosa', 'isiXhosa', true),
    ('zu', 'isiZulu', 'isiZulu', true),
    ('nso', 'Sepedi', 'Sepedi', true),
    ('st', 'Sesotho', 'Sesotho', true),
    ('tn', 'Setswana', 'Setswana', true),
    ('ss', 'siSwati', 'siSwati', true),
    ('ve', 'Tshivenda', 'Tshivenda', true),
    ('ts', 'Xitsonga', 'Xitsonga', true);

INSERT INTO reference.identifier_type (code, display_name, south_african_format) VALUES
    ('SOUTH_AFRICAN_ID', 'South African ID Number', true),
    ('PASSPORT', 'Passport Number', false),
    ('REFUGEE_PERMIT', 'Refugee Permit', false),
    ('BIRTH_CERTIFICATE', 'Birth Certificate Number', false),
    ('TEMPORARY_HEALTHCARE_NUMBER', 'Temporary Healthcare Number', false);

INSERT INTO reference.code_system (code, display_name, uri) VALUES
    ('ICD10', 'ICD-10', 'http://hl7.org/fhir/sid/icd-10'),
    ('LOINC', 'LOINC', 'http://loinc.org'),
    ('SNOMED_CT', 'SNOMED CT', 'http://snomed.info/sct');

INSERT INTO finance.payer (code, legal_name, payer_type) VALUES
    ('DISCOVERY', 'Discovery Health Medical Scheme', 'MEDICAL_AID'),
    ('BONITAS', 'Bonitas Medical Fund', 'MEDICAL_AID'),
    ('MOMENTUM', 'Momentum Health', 'MEDICAL_AID'),
    ('GEMS', 'Government Employees Medical Scheme', 'MEDICAL_AID'),
    ('BESTMED', 'Bestmed Medical Scheme', 'MEDICAL_AID'),
    ('MEDIHELP', 'Medihelp Medical Scheme', 'MEDICAL_AID'),
    ('FEDHEALTH', 'Fedhealth Medical Scheme', 'MEDICAL_AID');

GRANT SELECT ON ALL TABLES IN SCHEMA reference TO ${appRole};
GRANT SELECT ON TABLE finance.payer TO ${appRole};
