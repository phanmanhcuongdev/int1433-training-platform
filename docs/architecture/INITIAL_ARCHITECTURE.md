# Initial Architecture

## Scope

Version `0.4.1` keeps the v0.4 automatic grading slice and adds production image delivery through GHCR plus pull-only Docker Compose deployment.

Backend, PostgreSQL persistence, REST API, Vue Router, Docker-isolated Java runner, raw TCP/UDP challenge handlers, RMI service, SOAP/WSDL service and starter downloads are included. Authentication, leaderboards, Kubernetes, production deployment automation and arbitrary multi-language judging are not included.

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

Practice flow:

```text
Vue practice route
        |
        | /api/v1 submissions or challenge sessions
        v
Spring Boot
        |
        +--> Docker Java runner for JAVA_CODE
        |
        +--> TCP/UDP/RMI/SOAP challenge handlers for NETWORK_CHALLENGE
        |
        v
PostgreSQL verdict/history
```

Production runtime:

```text
Internet
   |
   v
web image: Nginx :80
   |          |
   |          +--> Vue SPA
   |
   +--> /api and /ws HTTP proxy
              |
              v
backend image: Spring Boot :8080
   |          |
   |          +--> Docker socket -> on-demand Java runner image
   |
   v
postgres image, internal network only
```

Raw TCP, UDP and RMI challenge ports are published directly by the backend container. They are not proxied through HTTP Nginx locations.

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

1. Review v0.4 runner and challenge boundaries.
2. Run production smoke checks on the target VM before opening raw challenge ports broadly.
3. Expand content only after the ten graded flows remain stable.

## Non-goals

- No official PTIT exam emulation.
- No submitted code execution outside the isolated Docker runner.
- No real exam server integration.
- No account authentication.
