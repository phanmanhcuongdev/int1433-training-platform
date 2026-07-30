# Content

This directory stores exercise and mock exam content.

Current status:

- JSON Schema is in `schema/exercise.schema.json`.
- Three draft pilot exercises are included.
- `npm run prepare-content` generates `web/public/generated/exercises.json`.
- No full exercise bank has been created.
- No judge metadata is executable yet.

All Exam Track content should include source traceability and must avoid hard-coding old real exam IPs or ports.

## Lifecycle

- `DRAFT`: usable for UI/content pilot, not reviewed as final learning material.
- `REVIEWED`: reviewed for wording, traceability, and technical correctness.
- `PUBLISHED`: ready for learners in the static catalog.
- `DEPRECATED`: retained for history but hidden or marked as outdated later.

The generated index is a build artifact. Edit source JSON under `content/exercises/`, then regenerate it.
