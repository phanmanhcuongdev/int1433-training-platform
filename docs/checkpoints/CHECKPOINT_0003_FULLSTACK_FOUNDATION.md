# Checkpoint 0003 - Full-stack Foundation

Date: 2026-07-30

Version: `0.3.0`

Commit/tag: intended commit `feat: establish full-stack exercise catalog`, tag `v0.3.0`.

## Completed

- Added Spring Boot backend under `backend/`.
- Added PostgreSQL schema and three seeded pilot exercises through Flyway.
- Added `/api/v1/exercises`, `/api/v1/exercises/{id}`, and `/api/v1/exercises/filters`.
- Added structured JSON error responses.
- Added Vue Router routes and API-driven catalog/detail pages.
- Added Vietnamese UI with proper diacritics.
- Added development PostgreSQL Compose file.
- Added Nginx reverse proxy example with SPA fallback.

## Important Files

- `backend/pom.xml`
- `backend/src/main/java/vn/edu/ptit/int1433/training/`
- `backend/src/main/resources/db/migration/`
- `web/src/router/index.js`
- `web/src/api/`
- `web/src/views/`
- `deploy/docker-compose.dev.yml`
- `deploy/nginx/int1433.conf`

## Commands

Start PostgreSQL:

```bash
docker compose -f deploy/docker-compose.dev.yml up -d
```

Run backend:

```bash
npm run backend:run
```

Run frontend:

```bash
npm run frontend:dev
```

Check:

```bash
npm run check
```

## Limits

- No local judge.
- No online judge.
- No user authentication.
- No file upload.
- No arbitrary code execution.
- No additional exercises beyond the three pilots.

## Allowed Next Tasks

- Review API response shape and UI.
- Add at most 9 exercises to reach 12 pilot exercises.
- Add an internal seed/import CLI after reviewing data model.

Do not start judge or sandbox work before the catalog flow and content model are reviewed.
