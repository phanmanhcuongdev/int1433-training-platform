# Initial Architecture

## Scope

Version `0.3.0` establishes a conventional full-stack catalog application.

Backend, PostgreSQL persistence, REST API, Vue Router, and deployment examples are included. Authentication, local judge, online judge, sandbox, user submissions, Kubernetes, and microservices are not included.

## Components

```text
Vue Router frontend
        |
        | HTTP /api/v1
        v
Spring Boot REST API
        |
        v
PostgreSQL
```

Authoring flow:

```text
content/exercises/*.json
        |
validate-content.mjs / build-content-index.mjs
        |
reviewed seed/import data
        |
Flyway/PostgreSQL
```

## Near-term Direction

1. Review API-driven UI and PostgreSQL seed data.
2. Expand to at most 12 pilot exercises.
3. Keep local judge/sandbox/user submissions out of scope until content review is complete.

## Non-goals

- No official PTIT exam emulation.
- No untrusted code execution.
- No real exam server integration.
- No database or user accounts.
