# ADR-0005: Spring Boot PostgreSQL Backend

## Status

Accepted

## Context

The project needs conventional catalog management and API consumption before any judge work. Static generated JSON was useful for bootstrap, but it does not exercise real application flow.

## Decision

Use Java 21, Spring Boot 3.x, Spring Web, Spring Data JPA, Bean Validation, PostgreSQL, Flyway, and Actuator health.

PostgreSQL is the runtime source of truth. JSON under `content/exercises/` remains the versioned authoring/import source.

## Consequences

- Frontend uses `/api/v1` instead of reading generated static JSON.
- Flyway owns schema and seed data.
- Backend does not execute submitted code.
- Spring Security and authentication remain deferred.
