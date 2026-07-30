# ADR-0004: Build-time Content Index

## Status

Accepted

## Context

The frontend needs to render real exercise content, but the project is still content-only. Adding a database or API now would increase operational work before the schema and pilot content are stable.

## Decision

Generate `web/public/generated/exercises.json` at build time from `content/exercises/**/*.json`.

The generated file is a build artifact and is ignored by Git. `npm run dev` and `npm run build` run `npm run prepare-content` first, so the frontend receives a current static JSON catalog.

## Consequences

- Exercise source remains versioned as small JSON files.
- The Vue app fetches a single static file and does not need backend code.
- No absolute filesystem paths are included in runtime content.
- Generated output must be regenerated before serving or building the frontend.

## Why Not Database/API Yet

Phase 1A only needs browsing, search, filters, and detail rendering for a small pilot set. A database/API would add migration, hosting, security, and deployment concerns without improving the immediate learning workflow.
