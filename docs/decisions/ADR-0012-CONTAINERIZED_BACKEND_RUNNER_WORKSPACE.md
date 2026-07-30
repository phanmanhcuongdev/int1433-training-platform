# ADR-0012: Containerized Backend Runner Workspace

Date: 2026-07-30

Status: Accepted for `0.4.1`.

## Context

The backend launches Java runner containers by calling the trusted Docker daemon through `/var/run/docker.sock`. When the backend itself runs in a container, a temporary path inside that backend container is not necessarily visible to the host Docker daemon that resolves bind mounts.

## Decision

Use one shared absolute workspace root:

```text
/var/lib/int1433/runner-workspaces
```

Production Compose bind-mounts the same host path into the backend container at the same absolute path. The backend creates random per-submission child directories inside this root, validates canonical containment, mounts that child directory into the runner container, then cleans it after verdict assignment.

## Consequences

- Containerized backend runner execution can bind-mount a path the host Docker daemon can resolve.
- `JAVA_RUNNER_WORKSPACE_ROOT` is mandatory in production.
- The workspace root must be writable by the non-root backend and runner users; deployment scripts create it with restrictive operational guidance.
- Docker socket access remains effectively host-level privilege and must not be exposed publicly.
- A future dedicated runner service can remove Docker socket access from the backend.
