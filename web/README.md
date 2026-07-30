# Web

Vue 3 + Vite app for the full-stack exercise catalog.

The app calls the Spring Boot REST API with relative URLs. In development, Vite proxies `/api` and `/actuator` to `http://localhost:8080`.

## Runtime Flow

```text
Vue Router page
        |
web/src/api/*
        |
/api/v1 through Vite proxy
        |
Spring Boot
```

Generated JSON remains ignored and is no longer the frontend runtime data source.

## Component Structure

- `App.vue`: top-level layout and navigation.
- `router/index.js`: routes for `/`, `/exercises`, `/exercises/:id`, `/exercises/:id/practice`, `/submissions/:submissionId`, `/challenge-sessions/:sessionId`, `/about`, and 404.
- `api/`: centralized fetch/error handling.
- `views/`: page-level data loading.
- `components/ProjectNotice.vue`: project status and disclaimer.
- `components/ExerciseFilters.vue`: search and metadata filters.
- `components/ExerciseCard.vue`: catalog card.
- `components/ExerciseDetail.vue`: detail view.
- `components/SourceLabelBadge.vue`: source label display.

Practice pages call backend endpoints for Java submissions and network challenge sessions. The UI never asks learners to self-report completion.

## Commands

```bash
npm install
npm run frontend:dev
npm run frontend:build
```
