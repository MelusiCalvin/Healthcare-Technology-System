#!/bin/sh
set -eu

: "${APP_DATABASE_USERNAME:?APP_DATABASE_USERNAME must be set}"
: "${APP_DATABASE_PASSWORD:?APP_DATABASE_PASSWORD must be set}"
: "${POSTGRES_DB:?POSTGRES_DB must be set}"

psql --set=ON_ERROR_STOP=1 \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=app_user="$APP_DATABASE_USERNAME" \
  --set=app_password="$APP_DATABASE_PASSWORD" \
  --set=database_name="$POSTGRES_DB" <<'EOSQL'
SELECT format(
    'CREATE ROLE %I LOGIN PASSWORD %L NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT',
    :'app_user',
    :'app_password'
)
WHERE NOT EXISTS (
    SELECT 1 FROM pg_catalog.pg_roles WHERE rolname = :'app_user'
)
\gexec

REVOKE ALL ON DATABASE :"database_name" FROM PUBLIC;
GRANT CONNECT ON DATABASE :"database_name" TO :"app_user";
EOSQL
