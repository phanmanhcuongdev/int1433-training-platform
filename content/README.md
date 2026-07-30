# Content

This directory stores exercise and mock exam content.

Current status:

- JSON Schema is in `schema/exercise.schema.json`.
- Ten reviewed exercises are included.
- `npm run prepare-content` generates `web/public/generated/exercises.json`.
- PostgreSQL is the runtime source for the application as of version `0.3.0`.
- As of `0.4.0`, exercise JSON includes evaluation metadata for Java code and network challenge flows.
- As of `0.4.1`, production images consume PostgreSQL runtime data; JSON remains authoring/import input only.

All Exam Track content should include source traceability and must avoid hard-coding old real exam IPs or ports.

## Lifecycle

- `DRAFT`: usable for UI/content pilot, not reviewed as final learning material.
- `REVIEWED`: reviewed for wording, traceability, and technical correctness.
- `PUBLISHED`: ready for learners in the static catalog.
- `DEPRECATED`: retained for history but hidden or marked as outdated later.

The generated index is a build artifact for authoring/import preparation. It is not the frontend runtime data source anymore. Edit source JSON under `content/exercises/`, then mirror reviewed changes into PostgreSQL with:

```bash
npm run content:import:dry
npm run content:import
npm run content:check
```
