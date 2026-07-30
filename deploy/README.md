# Deploy

This directory contains deployment helpers only. It does not deploy anything automatically.

## Development PostgreSQL

Start PostgreSQL:

```bash
docker compose -f deploy/docker-compose.dev.yml up -d
```

The compose file only runs PostgreSQL. Frontend and backend still run locally:

```bash
npm run backend:run
npm run frontend:dev
```

Safe development defaults are read from environment variables, with fallback values:

- `DB_HOST=localhost`
- `DB_PORT=5432`
- `DB_NAME=int1433`
- `DB_USER=int1433`
- `DB_PASSWORD=int1433_dev`

Native PostgreSQL can be used instead if it exposes the same database and credentials.

## Nginx Example

`nginx/int1433.conf` is an example production reverse proxy:

- `/` serves built Vue static files.
- `/api/` proxies to Spring Boot on `127.0.0.1:8080`.
- `/actuator/health` proxies health only.
- SPA routes fall back to `/index.html` for direct refresh/F5.

Do not expose all actuator endpoints publicly.

Raw TCP, UDP and RMI challenge traffic is not handled by normal HTTP `/api` reverse proxy rules. Production deployment must explicitly expose and firewall the configured challenge port ranges. SOAP uses the Spring Boot HTTP port at `/ws/factorization.wsdl` and `/ws`.

## Production Pull-Only Deployment

Production uses `docker-compose.prod.yml` and three GHCR application images:

- `ghcr.io/<owner>/int1433-backend:<version>`
- `ghcr.io/<owner>/int1433-web:<version>`
- `ghcr.io/<owner>/int1433-java-runner:<version>`

PostgreSQL uses the official `postgres:17-alpine` image. No PostgreSQL application image is built.

First deployment on the server:

```bash
mkdir -p /opt/int1433
cd /opt/int1433

# copy this repository's deploy/ directory to /opt/int1433/deploy
cp deploy/.env.prod.example deploy/.env.prod
nano deploy/.env.prod
# set DOCKER_GID with: stat -c '%g' /var/run/docker.sock

# Only needed when GHCR packages are private.
echo "$GHCR_TOKEN" | docker login ghcr.io -u "<github-user>" --password-stdin

./deploy/scripts/preflight.sh
./deploy/scripts/pull.sh
./deploy/scripts/up.sh
./deploy/scripts/smoke-test.sh
```

Generate the PostgreSQL password outside Git:

```bash
openssl rand -base64 36
```

Do not commit `deploy/.env.prod`, GitHub PATs, database passwords or server-specific secrets.

Set `DOCKER_GID` to the group id that the backend container sees for the Docker socket. Start with the host value:

```bash
stat -c '%g' /var/run/docker.sock
```

On hosts with id-mapped mounts, this value may differ inside containers. If backend logs show Docker socket permission errors, inspect the socket group inside the backend container and update `DOCKER_GID`.

## GHCR Authentication

Public packages can be pulled without `docker login`.

Private packages require a GitHub classic PAT with only `read:packages` on the server:

```bash
echo "$GHCR_TOKEN" | docker login ghcr.io -u "<github-user>" --password-stdin
```

Do not put PATs in Compose YAML, `.env.prod`, shell history, Docker images or this repository. GitHub Actions publishing uses `GITHUB_TOKEN`; external servers pulling private packages need registry credentials.

## Firewall

Inbound ports required when the service is public:

- `80/tcp`
- `443/tcp` if TLS is added later
- `19000-19020/tcp`
- `19100-19120/udp`
- `19200/tcp`

Do not expose:

- `5432`
- `8080`
- `5173`
- `2375`
- `2376`
- Proxmox management UI

UFW example, to be reviewed before running:

```bash
ufw allow 80/tcp
ufw allow 443/tcp
ufw allow 19000:19020/tcp
ufw allow 19100:19120/udp
ufw allow 19200/tcp
```

Raw TCP/UDP/RMI cannot pass through ordinary HTTP reverse proxy rules. `CHALLENGE_PUBLIC_HOST` and the RMI public host must resolve to the VM or router NAT endpoint.

## Operations

Update:

```bash
./deploy/scripts/update.sh
```

Rollback application images only:

```bash
./deploy/scripts/rollback.sh 0.4.0
```

Rollback does not reverse Flyway migrations. A database migration may make older app images incompatible.

Backup:

```bash
./deploy/scripts/backup-postgres.sh
```

Restore:

```bash
./deploy/scripts/restore-postgres.sh deploy/backups/<backup.sql.gz>
```

## External Smoke Checklist

From a laptop outside the VM:

- Domain resolves to the server.
- Frontend loads through HTTP or HTTPS.
- `/api/v1/exercises` returns ten exercises.
- Java foundation submission returns a platform verdict.
- TCP challenge port is reachable and returns a verdict after a local Java client connects.
- UDP challenge port is reachable and preserves requestId behavior.
- RMI registry lookup works with the configured public host and service name.
- SOAP WSDL is reachable and generated client request/submit works.
