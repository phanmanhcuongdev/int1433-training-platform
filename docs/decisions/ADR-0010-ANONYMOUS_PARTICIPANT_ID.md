# ADR-0010: Anonymous Participant ID

Date: 2026-07-30

## Status

Accepted for `0.4.0`.

## Context

The platform needs participant-bound submissions and challenge sessions, but account authentication is out of scope for this release.

## Decision

The frontend creates a UUID and sends it as `X-Participant-Id`. The backend validates UUID format and binds submissions/sessions to that participant ID.

## Consequences

- This is not secure identity and must not be treated as authentication.
- Attempt history works for a browser profile.
- Future account support can replace or link anonymous IDs later.
