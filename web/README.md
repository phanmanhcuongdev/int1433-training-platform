# Web

Vue 3 + Vite app for Phase 1 content-only rendering.

The app loads `/generated/exercises.json`, which is generated from source JSON files under `content/exercises/`.

## Content Loading Flow

```text
content/exercises/**/*.json
        |
npm run prepare-content
        |
web/public/generated/exercises.json
        |
Vue frontend fetch()
```

`web/public/generated/exercises.json` is ignored because it is a build artifact.

## Component Structure

- `App.vue`: SPA state, filtering, selected exercise state.
- `components/ProjectNotice.vue`: project status and disclaimer.
- `components/ExerciseFilters.vue`: search and metadata filters.
- `components/ExerciseCard.vue`: catalog card.
- `components/ExerciseDetail.vue`: detail view.
- `components/SourceLabelBadge.vue`: source label display.
- `composables/useExercises.js`: generated JSON fetch and response validation.

## Commands

```bash
npm install
npm run prepare-content
npm run dev
npm run build
```
