CREATE SCHEMA IF NOT EXISTS identity;

CREATE TABLE identity.user_account (
    id uuid PRIMARY KEY,
    username varchar(120) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE identity.user_role (
    user_id uuid NOT NULL REFERENCES identity.user_account(id) ON DELETE CASCADE,
    role varchar(40) NOT NULL,
    PRIMARY KEY (user_id, role)
);

CREATE INDEX idx_user_role_role ON identity.user_role(role);
