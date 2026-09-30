ALTER TABLE identity.user_account
    ADD COLUMN first_name varchar(120),
    ADD COLUMN last_name varchar(120),
    ADD COLUMN email varchar(120),
    ADD COLUMN sex varchar(10);

CREATE UNIQUE INDEX user_account_email_unique_idx
    ON identity.user_account (email)
    WHERE email IS NOT NULL;