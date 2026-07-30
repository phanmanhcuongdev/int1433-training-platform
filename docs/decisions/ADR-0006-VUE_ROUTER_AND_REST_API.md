# ADR-0006: Vue Router and REST API

## Status

Accepted

## Context

Exercise detail navigation used local state, so direct refresh on a detail page was not supported. The frontend also read generated JSON directly.

## Decision

Use Vue Router with browser history and REST API calls through a centralized API layer.

Routes:

- `/`
- `/exercises`
- `/exercises/:id`
- `/about`
- `/:pathMatch(.*)*`

Vite proxies `/api` and `/actuator` to Spring Boot during development. Production uses Nginx SPA fallback and reverse proxy.

## Consequences

- Detail routes are reload-safe.
- Filter query parameters are shareable.
- Frontend no longer depends on generated static JSON at runtime.
- Production servers must configure SPA fallback.
