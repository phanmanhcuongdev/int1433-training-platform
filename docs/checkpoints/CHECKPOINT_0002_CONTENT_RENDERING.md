# Checkpoint 0002 - Content Rendering

Date: 2026-07-30

Version: `0.2.0`

Commit/tag: intended commit `feat: render pilot exercises from content`, tag `v0.2.0`.

## Completed

- Reviewed schema and three pilot exercises for UI rendering.
- Added optional `summary`, `learning_objectives`, and `display_order` fields.
- Added build-time exercise index generation.
- Added frontend fetch from `/generated/exercises.json`.
- Added catalog cards, search, filters, detail view, and loading/error/empty states.
- Improved content validation.

## Important Files

- `scripts/validate-content.mjs`
- `scripts/build-content-index.mjs`
- `scripts/test-content-build.mjs`
- `content/schema/exercise.schema.json`
- `content/exercises/foundation/fnd-character-flush-001.json`
- `content/exercises/tcp/tcp-character-001.json`
- `content/exercises/udp/udp-string-request-id-001.json`
- `web/src/App.vue`
- `web/src/components/`
- `web/src/composables/useExercises.js`

## Commands

Validate content:

```bash
npm run validate
```

Generate content index:

```bash
npm run prepare-content
```

Run web app:

```bash
npm run dev
```

Build and check:

```bash
npm run build
npm run check
```

## Limits

- No backend.
- No database.
- No authentication.
- No local judge.
- No online judge.
- No new exercise beyond the initial three pilots.

## Allowed Next Tasks

- Review UI and schema with the three rendered pilots.
- Add at most 9 exercises to reach 12 pilot exercises.
- Improve content wording and traceability.

Do not start a local judge or backend before schema and the rendered pilot content are reviewed.
