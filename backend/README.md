# Backend

Spring Boot REST API for the INT1433 exercise catalog.

## Stack

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Flyway
- Actuator health
- JUnit 5 against the local development PostgreSQL instance

No Spring Security, authentication, judge, sandbox, or code execution is implemented in this version.

## Configuration

Runtime configuration comes from environment variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `SERVER_PORT`

See `.env.example` at repository root. Spring does not read `.env` automatically; export variables in your shell or use Docker Compose for PostgreSQL.

## Commands

Run tests:

```bash
docker compose -f deploy/docker-compose.dev.yml up -d
./backend/mvnw test
```

Run the backend in dev profile:

```bash
./backend/mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

API base path:

```text
/api/v1
```

Health:

```text
/actuator/health
```

## Data Source

PostgreSQL is the runtime source of truth. JSON files under `content/exercises/` remain versioned authoring/import sources and traceability snapshots.
