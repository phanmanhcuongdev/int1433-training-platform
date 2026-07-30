# ADR-0003: No Online Judge Yet

## Status

Accepted

## Context

An online judge requires running untrusted code, which introduces arbitrary code execution, filesystem, network, memory, CPU, and container escape risks.

## Decision

Do not build an online judge in the bootstrap. Do not run untrusted submissions on the host. Local judge and online judge work are deferred until schema and pilot content are reviewed.

## Consequences

- No `judge/`, `backend/`, `database/`, or `infrastructure/` directories.
- No Docker or sandbox implementation in this version.
- Future judge design must start with a security review.

