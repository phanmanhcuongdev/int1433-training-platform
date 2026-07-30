# Initial Architecture

## Scope

Version `0.2.0` is a Phase 1A start for a content-only platform.

No backend, database, authentication, local judge, online judge, Docker, or infrastructure is included.

## Components

```text
content/exercises/*.json
        |
        v
scripts/validate-content.mjs
        |
        v
scripts/build-content-index.mjs
        |
        v
web/public/generated/exercises.json
        |
        v
Vue frontend fetches generated catalog
```

## Near-term Direction

1. Review schema and pilot content in the rendered UI.
2. Expand to at most 12 pilot exercises.
3. Keep local judge/backend out of scope until content review is complete.

## Non-goals

- No official PTIT exam emulation.
- No untrusted code execution.
- No real exam server integration.
- No database or user accounts.
