# Checkpoint 0001 - Bootstrap

Date: 2026-07-30

Version: `0.1.0`

Commit/tag: intended initial commit `chore: bootstrap INT1433 training platform`, tag `v0.1.0`.

## Decisions

- Java is the Exam Track language.
- Phase 1 starts as content-only.
- No online judge, backend, database, Docker, or Kubernetes in bootstrap.
- Research snapshots are copied as Markdown only.

## Important Files

- `README.md`
- `content/schema/exercise.schema.json`
- `scripts/validate-content.mjs`
- `docs/research/exam/`
- `docs/research/platform/`
- `web/`

## Pilot Exercises

- `content/exercises/foundation/fnd-character-flush-001.json`
- `content/exercises/tcp/tcp-character-001.json`
- `content/exercises/udp/udp-string-request-id-001.json`

## Commands

Validate content:

```bash
npm run validate
```

Run web app:

```bash
npm install
npm run dev
```

Build web app:

```bash
npm run build
```

## Not Done

- No backend.
- No database.
- No local judge.
- No online judge.
- No authentication.
- No expanded exercise bank.

## Allowed Next Tasks

- Review schema.
- Render the three pilot exercises in the UI.
- Expand to at most 12 pilot exercises.

Do not start a local judge or backend before the schema and three pilot exercises are reviewed.

