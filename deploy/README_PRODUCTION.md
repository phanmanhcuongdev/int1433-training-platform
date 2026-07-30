# Production Deployment

This project uses pull-only deployment. GitHub Actions builds and pushes OCI images to GHCR; the server pulls images and starts Docker Compose. GitHub Actions does not SSH into the server.

## Files

- `docker-compose.prod.yml`: production services.
- `.env.prod.example`: environment template without secrets.
- `scripts/preflight.sh`: validates server prerequisites.
- `scripts/pull.sh`: pulls backend, web, runner and PostgreSQL images.
- `scripts/up.sh`: starts services and waits for web health.
- `scripts/smoke-test.sh`: checks frontend, API, SOAP WSDL and Java runner path.
- `scripts/update.sh`: backup, pull, restart, smoke test.
- `scripts/rollback.sh`: restart with an explicit previous application image version.

## Runtime Topology

```text
web container :80
  -> Vue SPA
  -> /api/, /ws, /actuator/health proxy to backend:8080

backend container :8080 internal
  -> PostgreSQL internal
  -> Docker socket for short-lived Java runner containers
  -> raw TCP/UDP/RMI challenge ports published directly

postgres container internal only
```

The Java runner is an on-demand image, not a long-running Compose service.

## First Deployment

```bash
mkdir -p /opt/int1433
cd /opt/int1433

# copy deploy/ from the repository release bundle
cp deploy/.env.prod.example deploy/.env.prod
nano deploy/.env.prod
# set DOCKER_GID with: stat -c '%g' /var/run/docker.sock

./deploy/scripts/preflight.sh
./deploy/scripts/pull.sh
./deploy/scripts/up.sh
./deploy/scripts/smoke-test.sh
```

If GHCR packages are private:

```bash
echo "$GHCR_TOKEN" | docker login ghcr.io -u "<github-user>" --password-stdin
```

Use a GitHub classic PAT with `read:packages` only.

## GitHub Repository Setup

After creating the GitHub repository, add a remote and push manually:

```bash
git remote add origin git@github.com:<owner>/<repo>.git
git push -u origin main
git push origin --tags
```

Required repository settings:

- GitHub Actions enabled.
- Workflow `GITHUB_TOKEN` allowed to write packages.
- GHCR packages linked to the repository.
- Package visibility selected: public for unauthenticated pulls, private for restricted pulls.
- Branch protection recommended for `main`.

Publishing from GitHub Actions uses `GITHUB_TOKEN`. Server pulls from private GHCR packages require a separate `read:packages` token on the server.

## Required Ports

- `80/tcp`
- `443/tcp` when TLS is added
- `19000-19020/tcp`
- `19100-19120/udp`
- `19200/tcp`

Do not expose PostgreSQL `5432`, backend `8080`, Vite `5173`, Docker TCP sockets, or the Proxmox UI.

## Runner Workspace

The backend and host Docker daemon must agree on the same absolute workspace root:

```text
/var/lib/int1433/runner-workspaces
```

Production Compose bind-mounts this host path into the backend container at the same path. The backend creates random per-submission child directories only inside this root and removes them after verdict assignment.

Docker socket access is privileged. Treat the backend container as trusted infrastructure and expose only the web container publicly.

The backend image runs as a non-root user. Production Compose adds the socket group id through `DOCKER_GID`; start with:

```bash
stat -c '%g' /var/run/docker.sock
```

On hosts with id-mapped mounts, the group seen inside the container may differ from host `stat`. If Docker CLI inside backend reports permission denied, inspect `/var/run/docker.sock` from inside the backend container and set `DOCKER_GID` to that numeric group.
