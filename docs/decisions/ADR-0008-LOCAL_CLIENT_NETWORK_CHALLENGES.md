# ADR-0008: Local Client Network Challenges

Date: 2026-07-30

## Status

Accepted for `0.4.0`.

## Context

INT1433 exam-like tasks require students to write clients that connect to TCP, UDP, RMI and SOAP services. A normal REST-only judge would miss stream ordering, flush, requestId and service-contract errors.

## Decision

The platform starts challenge sessions and exposes bounded development ports/endpoints. Learners run Java clients locally. The backend records protocol traces and assigns verdicts without manual completion confirmation.

## Consequences

- TCP/UDP/RMI traffic is not reverse-proxied by normal HTTP `/api` rules.
- Port ranges must be configured and firewalled in any real deployment.
- Session tokens are shown once to the browser and stored hashed server-side.
