# Checkpoint 0005: GHCR Deployment

Date: 2026-07-30

Version: `0.4.1`

## Scope

Production packaging and pull-only deployment for the existing ten automatically graded exercises.

## Images

- `ghcr.io/<owner>/int1433-backend:0.4.1`
- `ghcr.io/<owner>/int1433-web:0.4.1`
- `ghcr.io/<owner>/int1433-java-runner:0.4.1`

Release tags also publish `0.4`, `sha-<short-sha>` and `latest`. Main pushes publish `main` and `sha-<short-sha>`.

## Runtime

- Web/Nginx: public `80/tcp`.
- Backend/Spring Boot: internal `8080`.
- PostgreSQL: internal only.
- TCP challenges: `19000-19020/tcp`.
- UDP challenges: `19100-19120/udp`.
- RMI registry: `19200/tcp`.
- SOAP: HTTP `/ws/factorization.wsdl` through web proxy.

## Runner Boundary

Backend mounts:

- `/var/run/docker.sock`
- `/var/lib/int1433/runner-workspaces:/var/lib/int1433/runner-workspaces`

This is trusted infrastructure. The backend must not be exposed directly to the Internet.

## Commands

```bash
npm run check
docker compose -f deploy/docker-compose.prod.yml config
docker build -t ghcr.io/local/int1433-backend:0.4.1 -f backend/Dockerfile .
docker build -t ghcr.io/local/int1433-web:0.4.1 -f web/Dockerfile .
docker build -t ghcr.io/local/int1433-java-runner:0.4.1 runner
```

Server deployment:

```bash
cp deploy/.env.prod.example deploy/.env.prod
./deploy/scripts/preflight.sh
./deploy/scripts/pull.sh
./deploy/scripts/up.sh
./deploy/scripts/smoke-test.sh
```

## Next Allowed Tasks

- Run production smoke on the target VM.
- Add TLS termination.
- Move Java runner execution behind a dedicated runner service after security review.

Do not add more exercises before the production deployment flow is tested on the VM.
