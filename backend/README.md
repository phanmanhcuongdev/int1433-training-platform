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
- Spring Web Services for the SOAP challenge
- JUnit 5 against the local development PostgreSQL instance

No Spring Security, authentication, leaderboard, or production deployment automation is implemented in this version.

## Configuration

Runtime configuration comes from environment variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `SERVER_PORT`
- `JAVA_RUNNER_IMAGE`
- `JAVA_RUNNER_WORKSPACE_ROOT`

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

Java code submission endpoints:

```text
POST /api/v1/exercises/{exerciseId}/submissions
Content-Type: multipart/form-data
file=<exercise-id>.java
```

The multipart endpoint is the browser workflow for `JAVA_CODE` exercises. Backend validation requires one UTF-8 `.java` file, exact filename `<exercise-id>.java`, maximum 20 KB, and source containing `public class Main`. The runner still writes source to `Main.java` inside the isolated workspace before compiling.

The older JSON endpoint remains for compatibility and for inline resubmission from the submitted source viewer:

```text
POST /api/v1/exercises/{exerciseId}/code-submissions
```

Each submission creates a new row. Submission detail responses include original filename, source SHA-256 and submitted source text for the owning anonymous participant.

SOAP challenge WSDL:

```text
/ws/factorization.wsdl
```

Health:

```text
/actuator/health
```

## Data Source

PostgreSQL is the runtime source of truth. JSON files under `content/exercises/` remain versioned authoring/import sources and traceability snapshots.

Development content import:

```bash
npm run content:import:dry
npm run content:import
npm run content:check
```

The backend owns parsing, validation and transactional upsert. The import command is intended for the `dev` profile only.

## Production Container

`backend/Dockerfile` builds a Java 21 runtime image with Docker CLI installed. It does not contain Node.js, Maven cache or the source repository.

The backend invokes the trusted host Docker daemon to run Java submissions in the separate runner image. In containerized production this requires:

- Docker socket mounted at `/var/run/docker.sock`.
- Identical host/container workspace path: `/var/lib/int1433/runner-workspaces`.
- `JAVA_RUNNER_WORKSPACE_ROOT=/var/lib/int1433/runner-workspaces`.
- `JAVA_RUNNER_IMAGE=ghcr.io/<owner>/int1433-java-runner:<version>`.

This is privileged infrastructure. Do not expose backend port `8080` publicly; expose only the web/Nginx container.
