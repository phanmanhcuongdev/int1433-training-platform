# Roadmap

## Phase 0 - Bootstrap

Status: complete in `0.1.0`.

- Initialize repository.
- Copy research snapshots.
- Define initial JSON content schema.
- Add three draft pilot exercises.
- Add minimal Vue/Vite frontend.
- Add content validator.

## Phase 1A - Static Site + 12 Exercises

Status: evolved into full-stack catalog foundation in `0.3.0`.

- Render pilot exercises in the UI. Complete for the initial 3 draft exercises.
- Add Spring Boot REST API and PostgreSQL runtime catalog. Complete in `0.3.0`.
- Add Vue Router and reload-safe detail routes. Complete in `0.3.0`.
- Review schema and pilot content.
- Expand to at most 12 reviewed exercises.
- Keep judge, sandbox, authentication, and user submissions out of scope.

## Phase 1B - 24 Exercises

- Expand balanced content across foundation, TCP, UDP, RMI, Web Service, and debugging.
- Add better filters and content navigation.

## Phase 2 - Local Judge Pilot

- Add local mock server/harness only after schema and pilot content are reviewed.
- Start with a small Java-only pilot.

## Phase 3 - Online Judge

- Only after security review.
- Never run untrusted submissions directly on the host.
