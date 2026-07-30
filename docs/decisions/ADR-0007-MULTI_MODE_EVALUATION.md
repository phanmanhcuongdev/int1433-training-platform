# ADR-0007: Multi-Mode Evaluation

Date: 2026-07-30

## Status

Accepted for `0.4.0`.

## Context

INT1433 practice needs more than static statements. Foundation Java I/O can be graded from submitted source code, while TCP/UDP/RMI/SOAP exercises need a real server-side protocol session.

## Decision

Support `JAVA_CODE` and `NETWORK_CHALLENGE` evaluation modes in the same catalog model. `JAVA_CODE` runs only through the isolated Docker runner. `NETWORK_CHALLENGE` creates participant-bound sessions and stores verdicts automatically.

## Consequences

- Exercise content must declare `evaluation_mode` and `grader_key`.
- UI practice pages choose the interaction model from backend metadata.
- The platform still does not support arbitrary multi-language judging.
