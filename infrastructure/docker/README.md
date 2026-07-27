# Local Infrastructure

Copy the root `.env.example` file to `.env`, replace every placeholder password, then run:

```powershell
docker compose up -d
docker compose logs flyway
```

PostgreSQL, Redis, RabbitMQ, and MinIO bind only to `127.0.0.1`. Flyway runs once after PostgreSQL becomes healthy. `docker compose down` preserves volumes; use `docker compose down --volumes` only when intentionally discarding all local development data.

The PostgreSQL owner account performs migrations. The Spring Boot application uses `APP_DATABASE_USERNAME`, which has data permissions but no schema-creation privileges.
