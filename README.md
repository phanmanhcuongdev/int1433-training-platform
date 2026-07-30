# INT1433 Training Platform

Unofficial training platform for INT1433 - Lap trinh mang.

This project is not an official PTIT exam system, not a replacement for course announcements, and not a source of current exam server IPs, ports, or credentials.

## Goal

Build a Java-first practice platform for INT1433 with automatic grading for selected Java I/O and network protocol exercises. This is still a training system, not an official exam service.

## Current Status

- Version: `0.4.2`
- State: ten automatically evaluated exercises
- Frontend: Vue 3/Vite with Vue Router and REST API calls
- Backend: Spring Boot REST API under `/api/v1`
- Database: PostgreSQL with Flyway migrations
- Content: 10 reviewed exercises
- Practice: Java code submissions plus TCP/UDP/RMI/SOAP challenge sessions
- Judge: Docker-isolated Java runner for foundation exercises
- Authentication: not implemented

## Tracks

- Exam Track: Java-first, close to TCP/UDP/RMI/SOAP Web Service exam contracts.
- Extended Networking Track: TCP/UDP and networking concepts beyond exam drills.
- Backend/Distributed Systems Track: later backend/distributed systems extensions, not part of default INT1433 mock exams.

## Stack

- Vue 3 + Vite + Vue Router + JavaScript.
- Java 21 + Spring Boot 3.x.
- PostgreSQL + Flyway.
- Docker-isolated Java runner image `int1433-java-runner:0.4.2`.
- Production images are published through GitHub Actions to GHCR.
- Spring Web Services publishes the SOAP/WSDL challenge endpoint.
- JSON content files under `content/exercises` remain authoring/import sources.
- Node.js validation and build-time content index scripts with no extra package dependency.

## Repository Structure

```text
backend/    Spring Boot REST API, Flyway migrations, tests.
content/    Exercise schema, draft exercises, future mock exam content.
deploy/     Development PostgreSQL compose, production Compose, Nginx and server scripts.
docs/       Research snapshots, architecture notes, ADRs, checkpoints.
scripts/    Content validation and generated index scripts.
web/        Vue/Vite frontend.
```

## Commands

Validate content without installing frontend dependencies:

```bash
npm run validate
```

Prepare the generated exercise index for authoring checks:

```bash
npm run prepare-content
```

Start development PostgreSQL:

```bash
docker compose -f deploy/docker-compose.dev.yml up -d
```

Run the backend:

```bash
npm run backend:run
```

Import content into the development database:

```bash
npm run content:import:dry
npm run content:import
npm run content:check
```

Run the frontend in another terminal:

```bash
npm install
npm run frontend:dev
```

Build the frontend:

```bash
npm run build
```

Run all checks:

```bash
npm run check
```

## Production Images

The production deployment is pull-only. The server does not need Node.js, Maven or the source tree to run the application after it has the deployment bundle.

Images:

- `ghcr.io/<owner>/int1433-backend`
- `ghcr.io/<owner>/int1433-web`
- `ghcr.io/<owner>/int1433-java-runner`

Release tag `v0.4.2` publishes `0.4.2`, `0.4`, `sha-<short-sha>` and `latest`. Pushes to `main` publish `main` and `sha-<short-sha>`. Pull requests build images but do not push.

Server flow:

```bash
mkdir -p /opt/int1433
cd /opt/int1433
cp deploy/.env.prod.example deploy/.env.prod
nano deploy/.env.prod
./deploy/scripts/preflight.sh
./deploy/scripts/pull.sh
./deploy/scripts/up.sh
./deploy/scripts/smoke-test.sh
```

Only the web container exposes normal HTTP. PostgreSQL and backend HTTP are internal. Raw TCP/UDP/RMI challenge ports are published explicitly and must be opened in the firewall only when this service is intentionally exposed.

The backend container mounts `/var/run/docker.sock` and the shared runner workspace `/var/lib/int1433/runner-workspaces` so it can launch short-lived Java runner containers. Docker socket access is effectively host-level privilege; expose the backend only through the Nginx web service.

## Frontend Features

- Loads `/api/v1/exercises` through the Vite proxy in development.
- Shows the ten reviewed exercises seeded/imported into PostgreSQL.
- Supports search by title, id, and tag.
- Filters by technology, level, and source label.
- Uses Vue Router routes: `/`, `/exercises`, `/exercises/:id`, `/exercises/:id/practice`, `/submissions/:submissionId`, `/challenge-sessions/:sessionId`, `/about`.
- Includes loading, error, malformed response, and empty-result states.
- Practice pages show verdicts from the platform; users do not self-report completion.

Generated content is no longer the frontend runtime data source.

## Roadmap

1. Phase 0: bootstrap, schema, 3 draft pilot exercises. Completed in `0.1.0`.
2. Phase 1A: static site renders pilot content and grows to reviewed exercises. Started in `0.2.0`.
3. Phase 2: automatic grading product slice for 10 exercises. Completed in `0.4.0`.
4. Phase 3: production image delivery and pull-only deployment bundle. Completed in `0.4.2`.
5. Phase 4: harden operations, security review, and expand content only after v0.4 flows are reviewed.

## Research

Initial research snapshots are stored in:

- [docs/research/exam](docs/research/exam)
- [docs/research/platform](docs/research/platform)

These are synthesized notes, not official PTIT documents.
