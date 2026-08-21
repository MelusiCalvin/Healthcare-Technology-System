INSERT INTO iam.role (code, display_name, description, system_role) VALUES
    ('SYSTEM_ADMIN', 'System Administrator', 'Platform-wide administration and governance.', true),
    ('PROVINCIAL_ADMIN', 'Provincial Administrator', 'Provincial health administration.', true),
    ('HOSPITAL_ADMIN', 'Hospital Administrator', 'Facility and departmental administration.', true),
    ('RECEPTIONIST', 'Receptionist', 'Patient reception and scheduling.', true),
    ('DOCTOR', 'Doctor', 'Clinical practitioner.', true),
    ('NURSE', 'Nurse', 'Nursing practitioner.', true),
    ('PHARMACIST', 'Pharmacist', 'Pharmacy practitioner.', true),
    ('LABORATORY_TECHNICIAN', 'Laboratory Technician', 'Laboratory practitioner.', true),
    ('RADIOLOGIST', 'Radiologist', 'Radiology practitioner.', true),
    ('EMS_OPERATOR', 'EMS Operator', 'Emergency medical services practitioner.', true),
    ('PATIENT', 'Patient', 'Patient self-service account.', true);

INSERT INTO iam.permission (code, display_name, description) VALUES
    ('SYSTEM:ADMINISTER', 'Administer Platform', 'Perform platform-wide administration.'),
    ('IAM:READ', 'Read Identity Data', 'View users, roles, and permissions.'),
    ('IAM:MANAGE', 'Manage Identity Data', 'Create and change users, roles, and permissions.'),
    ('AUDIT:READ', 'Read Audit Events', 'View compliance audit events.'),
    ('PATIENT:SELF:READ', 'Read Own Patient Information', 'Access a patient''s own health information.'),
    ('PATIENT:SELF:MANAGE', 'Manage Own Patient Preferences', 'Manage a patient''s own profile and preferences.');

INSERT INTO iam.role_permission (role_id, permission_id)
SELECT role.id, permission.id
FROM iam.role role
CROSS JOIN iam.permission permission
WHERE role.code = 'SYSTEM_ADMIN'
  AND permission.code IN ('SYSTEM:ADMINISTER', 'IAM:READ', 'IAM:MANAGE', 'AUDIT:READ');

INSERT INTO iam.role_permission (role_id, permission_id)
SELECT role.id, permission.id
FROM iam.role role
CROSS JOIN iam.permission permission
WHERE role.code = 'PATIENT'
  AND permission.code IN ('PATIENT:SELF:READ', 'PATIENT:SELF:MANAGE');
