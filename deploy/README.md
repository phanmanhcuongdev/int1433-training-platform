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
