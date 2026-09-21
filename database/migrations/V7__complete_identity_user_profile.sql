ALTER TABLE identity.user_account ADD COLUMN first_name varchar(120);
ALTER TABLE identity.user_account ADD COLUMN last_name varchar(120);
ALTER TABLE identity.user_account ADD COLUMN email varchar(120);
ALTER TABLE identity.user_account ADD COLUMN sex varchar(10);

UPDATE identity.user_account
SET first_name = COALESCE(NULLIF(first_name, ''), 'User'),
    last_name = COALESCE(NULLIF(last_name, ''), 'Account'),
    email = COALESCE(NULLIF(email, ''), lower(username) || '@local.invalid'),
    sex = COALESCE(NULLIF(sex, ''), 'OTHER')
WHERE first_name IS NULL OR last_name IS NULL OR email IS NULL OR sex IS NULL;

ALTER TABLE identity.user_account ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE identity.user_account ALTER COLUMN last_name SET NOT NULL;
ALTER TABLE identity.user_account ALTER COLUMN email SET NOT NULL;
ALTER TABLE identity.user_account ALTER COLUMN sex SET NOT NULL;
ALTER TABLE identity.user_account ADD CONSTRAINT user_account_email_unique UNIQUE (email);