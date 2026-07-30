# ADR-0009: Isolated Java Runner

Date: 2026-07-30

## Status

Accepted for `0.4.0`.

## Context

Foundation exercises need Java code submission, but untrusted code must not run in the Spring Boot process or directly on the host.

## Decision

Use a Docker image such as `int1433-java-runner:0.4.1` for Java 21 compilation and execution. The backend invokes Docker as trusted infrastructure and applies no network, read-only filesystem, memory, CPU, PID, timeout and output limits.

## Consequences

- Docker daemon access remains a privileged operational boundary.
- Runner tests must cover AC, WA, CE, RE, TLE, output cap, network blocked and cleanup.
- No Maven/Gradle dependency download is allowed inside submissions.
