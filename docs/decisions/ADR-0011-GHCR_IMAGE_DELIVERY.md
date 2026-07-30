# ADR-0011: GHCR Image Delivery

Date: 2026-07-30

Status: Accepted for `0.4.2`.

## Context

The production server should run the platform without Node.js, Maven or repository source code. GitHub Actions can build and publish images, but it must not SSH into or mutate the Proxmox VM.

## Decision

Build exactly three application OCI images:

- `ghcr.io/<owner>/int1433-backend`
- `ghcr.io/<owner>/int1433-web`
- `ghcr.io/<owner>/int1433-java-runner`

Use GHCR with tags:

- release `v0.4.2`: `0.4.2`, `0.4`, `sha-<short-sha>`, `latest`
- main push: `main`, `sha-<short-sha>`

Pull requests build images but do not push. The server deployment is pull-only with `docker compose pull` and `docker compose up -d`.

## Consequences

- The VM needs Docker Engine, Docker Compose and registry access only.
- GitHub does not need server SSH credentials.
- PostgreSQL remains the official upstream image, not an application image.
- Package visibility and GHCR pull credentials are operational concerns documented under `deploy/`.
