# Checkpoint 0006 - Production VM Deployment

Date: 2026-07-30

Version: `0.4.2`

## Purpose

Close the production deployment gap after `v0.4.1` by aligning GitHub Actions, GHCR image tags, Docker Compose, deployment scripts and the Ubuntu VM deployment flow.

## Release Target

- `ghcr.io/phanmanhcuongdev/int1433-backend:0.4.2`
- `ghcr.io/phanmanhcuongdev/int1433-web:0.4.2`
- `ghcr.io/phanmanhcuongdev/int1433-java-runner:0.4.2`

## Deployment Topology

- `web`: public HTTP on port `80`, Vue static files and reverse proxy.
- `backend`: internal Spring Boot API on port `8080`.
- `postgres`: internal PostgreSQL 17 service.
- Java runner: on-demand Docker image invoked by backend through the trusted Docker socket.

## Server Defaults

- Deployment directory: `/opt/int1433`
- Runner workspace: `/var/lib/int1433/runner-workspaces`
- Backup directory: `/var/backups/int1433`
- HTTP: `80/tcp`
- TCP challenges: `19000-19020/tcp`
- UDP challenges: `19100-19120/udp`
- RMI registry: `19200/tcp`

## Required Verification

```bash
npm run validate
npm run content:check
npm run starters:check
npm run frontend:build
npm run backend:test
npm run check
docker compose --env-file deploy/.env.prod.example -f deploy/docker-compose.prod.yml config
git diff --check
```

On the server:

```bash
./deploy/scripts/preflight.sh
./deploy/scripts/pull.sh
./deploy/scripts/up.sh
./deploy/scripts/status.sh
./deploy/scripts/smoke-test.sh
```

## Notes

- Do not move or reuse `v0.4.1`; it points to the earlier release commit.
- Use `v0.4.2` for deployment images.
- Do not expose PostgreSQL `5432`, backend `8080`, Vite `5173` or Docker TCP sockets publicly.
- Docker socket access remains trusted infrastructure and must not be exposed to arbitrary users.
