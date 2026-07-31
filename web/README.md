# Web

Vue 3 + Vite app for the full-stack exercise catalog.

The app calls the Spring Boot REST API with relative URLs. In development, Vite proxies `/api` and `/actuator` to `http://localhost:8080`.
In production, `web/Dockerfile` serves the built Vue app through Nginx and proxies `/api/`, `/ws`, `/ws/*` and `/actuator/health` to the backend service.

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

For `JAVA_CODE` exercises, the practice route keeps the exercise statement, examples, common failures and file upload on one page. Learners upload exactly one `.java` file whose name matches its top-level public class, then the submission route shows the verdict, hidden test summary, submitted source, copy action and an inline edit/resubmit flow with an editable filename field. This is intentionally not a full browser IDE and does not add a "run thử" button.

## Commands

```bash
npm install
npm run frontend:dev
npm run frontend:build
```

The production image never serves Vite on port `5173`; it listens on port `80` and supports direct refresh of SPA routes with `try_files $uri $uri/ /index.html`.
