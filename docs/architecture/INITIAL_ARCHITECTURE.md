# Initial Architecture

## Scope

Version `0.1.0` is a Phase 0 bootstrap for a content-only platform.

No backend, database, authentication, local judge, online judge, Docker, or infrastructure is included.

## Components

```text
content/exercises/*.json
        |
        v
scripts/validate-content.mjs
        |
        v
web/ minimal static Vue app
```

## Near-term Direction

1. Validate content JSON.
2. Render pilot exercises in the static frontend.
3. Review schema and pilot content.
4. Expand to at most 12 pilot exercises.

## Non-goals

- No official PTIT exam emulation.
- No untrusted code execution.
- No real exam server integration.
- No database or user accounts.

