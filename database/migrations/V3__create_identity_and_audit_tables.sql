CREATE TABLE iam.user_account (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username citext NOT NULL UNIQUE,
    email citext UNIQUE,
    display_name varchar(160) NOT NULL,
    status varchar(32) NOT NULL DEFAULT 'ACTIVE',
    last_login_at timestamptz,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version bigint NOT NULL DEFAULT 0,
    CONSTRAINT user_account_username_not_blank CHECK (char_length(btrim(username::text)) > 0),
    CONSTRAINT user_account_status_check CHECK (status IN ('ACTIVE', 'DISABLED', 'LOCKED', 'PASSWORD_RESET_REQUIRED'))
);

CREATE TABLE iam.password_credential (
    user_id uuid PRIMARY KEY REFERENCES iam.user_account(id) ON DELETE RESTRICT,
    password_hash varchar(100) NOT NULL,
    password_changed_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    must_change_password boolean NOT NULL DEFAULT false,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT password_credential_bcrypt_check CHECK (password_hash LIKE '$2%')
);

CREATE TABLE iam.role (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code varchar(64) NOT NULL UNIQUE,
    display_name varchar(120) NOT NULL,
    description varchar(500),
    system_role boolean NOT NULL DEFAULT false,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT role_code_format_check CHECK (code ~ '^[A-Z][A-Z0-9_]{1,63}$')
);

CREATE TABLE iam.permission (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code varchar(120) NOT NULL UNIQUE,
    display_name varchar(160) NOT NULL,
    description varchar(500),
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT permission_code_format_check CHECK (code ~ '^[A-Z][A-Z0-9_]*:[A-Z][A-Z0-9_:]*$')
);

CREATE TABLE iam.role_permission (
    role_id uuid NOT NULL REFERENCES iam.role(id) ON DELETE RESTRICT,
    permission_id uuid NOT NULL REFERENCES iam.permission(id) ON DELETE RESTRICT,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE iam.user_role (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL REFERENCES iam.user_account(id) ON DELETE RESTRICT,
    role_id uuid NOT NULL REFERENCES iam.role(id) ON DELETE RESTRICT,
    scope_type varchar(32) NOT NULL DEFAULT 'GLOBAL',
    scope_id uuid,
    assigned_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by_user_id uuid REFERENCES iam.user_account(id) ON DELETE RESTRICT,
    expires_at timestamptz,
    CONSTRAINT user_role_scope_check CHECK (
        (scope_type = 'GLOBAL' AND scope_id IS NULL)
        OR (scope_type <> 'GLOBAL' AND scope_id IS NOT NULL)
    ),
    CONSTRAINT user_role_unique UNIQUE NULLS NOT DISTINCT (user_id, role_id, scope_type, scope_id)
);

CREATE TABLE iam.refresh_token (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    token_hash char(64) NOT NULL UNIQUE,
    user_id uuid NOT NULL REFERENCES iam.user_account(id) ON DELETE RESTRICT,
    family_id uuid NOT NULL,
    replaced_by_token_id uuid UNIQUE REFERENCES iam.refresh_token(id) ON DELETE RESTRICT,
    issued_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at timestamptz NOT NULL,
    revoked_at timestamptz,
    revocation_reason varchar(64),
    last_used_at timestamptz,
    user_agent_hash char(64),
    source_ip_hash char(64),
    CONSTRAINT refresh_token_expiry_check CHECK (expires_at > issued_at),
    CONSTRAINT refresh_token_revocation_check CHECK (
        (revoked_at IS NULL AND revocation_reason IS NULL)
        OR (revoked_at IS NOT NULL AND revocation_reason IS NOT NULL)
    )
);

CREATE INDEX refresh_token_user_active_idx
    ON iam.refresh_token (user_id, expires_at)
    WHERE revoked_at IS NULL;

CREATE INDEX refresh_token_family_idx ON iam.refresh_token (family_id);

CREATE TABLE iam.login_attempt (
    subject_hash char(64) PRIMARY KEY,
    failure_count integer NOT NULL DEFAULT 0,
    window_started_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    locked_until timestamptz,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT login_attempt_failure_count_check CHECK (failure_count >= 0)
);

CREATE TABLE audit.audit_event (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    occurred_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    event_type varchar(120) NOT NULL,
    outcome varchar(32) NOT NULL,
    actor_user_id uuid,
    target_type varchar(120),
    target_id uuid,
    correlation_id varchar(128),
    source_ip_hash char(64),
    metadata jsonb NOT NULL DEFAULT '{}'::jsonb,
    CONSTRAINT audit_event_outcome_check CHECK (outcome IN ('SUCCESS', 'FAILURE', 'DENIED'))
);

CREATE INDEX audit_event_occurred_at_idx ON audit.audit_event (occurred_at DESC);
CREATE INDEX audit_event_actor_idx ON audit.audit_event (actor_user_id, occurred_at DESC) WHERE actor_user_id IS NOT NULL;
CREATE INDEX audit_event_target_idx ON audit.audit_event (target_type, target_id, occurred_at DESC) WHERE target_id IS NOT NULL;

CREATE OR REPLACE FUNCTION audit.prevent_audit_event_mutation()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    RAISE EXCEPTION 'Audit events are immutable';
END;
$$;

CREATE TRIGGER user_account_set_updated_at
    BEFORE UPDATE ON iam.user_account
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER password_credential_set_updated_at
    BEFORE UPDATE ON iam.password_credential
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER role_set_updated_at
    BEFORE UPDATE ON iam.role
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER permission_set_updated_at
    BEFORE UPDATE ON iam.permission
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER login_attempt_set_updated_at
    BEFORE UPDATE ON iam.login_attempt
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER audit_event_immutable
    BEFORE UPDATE OR DELETE ON audit.audit_event
    FOR EACH ROW EXECUTE FUNCTION audit.prevent_audit_event_mutation();
