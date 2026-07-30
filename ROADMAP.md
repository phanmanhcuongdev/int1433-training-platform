# Roadmap

## Phase 0 - Bootstrap

Status: complete in `0.1.0`.

- Initialize repository.
- Copy research snapshots.
- Define initial JSON content schema.
- Add three draft pilot exercises.
- Add minimal Vue/Vite frontend.
- Add content validator.

## Phase 1A - Static Site + Catalog

Status: evolved into full-stack catalog foundation in `0.3.0`.

- Render pilot exercises in the UI. Complete for the initial 3 draft exercises.
- Add Spring Boot REST API and PostgreSQL runtime catalog. Complete in `0.3.0`.
- Add Vue Router and reload-safe detail routes. Complete in `0.3.0`.
- Review schema and pilot content.
- Keep authentication and social features out of scope.

## Phase 2 - Ten Auto-Graded Exercises

Status: complete in `0.4.0`.

- Add Java code submission flow for two foundation exercises.
- Add TCP, UDP, RMI and SOAP challenge sessions for eight network exercises.
- Add starter downloads, participant-bound history, importer and consistency checks.
- Keep authentication, leaderboard and production deployment out of scope.

## Phase 1B - 24 Exercises

- Expand balanced content across foundation, TCP, UDP, RMI, Web Service, and debugging.
- Add better filters and content navigation.

## Phase 3 - Production Delivery

- Production OCI images, GHCR workflows and pull-only Compose deployment. Complete in `0.4.2`.

## Phase 4 - Practice UX Hardening

- File-based Java submission workflow. Complete in `0.5.0`.
- Submitted source viewer and inline resubmission. Complete in `0.5.0`.
- Keep network challenges on starter-project/local-client flow.

## Phase 5 - Hardening And Expansion

- Security review for runner and network services.
- Add more exercises only after v0.5 flows are reviewed.
- Improve operational docs and deployment boundaries.

## Later

- Only after security review.
- Never run untrusted submissions directly on the host.
