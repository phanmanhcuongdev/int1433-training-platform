# Changelog

## [Unreleased]

## [0.6.0] - 2026-07-31

### Added

- Dynamic Java entry class support for `JAVA_CODE` submissions.
- Persisted `entryClassName` metadata for Java submissions.
- Editable filename support for inline source resubmission.
- Validation for Java filename, public class and main entrypoint consistency.

### Changed

- Java source filenames must now match their top-level public class.
- The runner compiles the validated original Java filename and executes its detected entry class.
- `JAVA_CODE` submissions no longer require `<exercise-id>.java` or `public class Main`.

### Fixed

- Local Java files can now be uploaded directly without renaming them before submission.
- Existing submissions using `Main.java` remain readable and resubmittable.

## [0.5.2] - 2026-07-30

### Fixed

- Production preflight now allows in-place updates when the current Compose deployment already owns the published HTTP and challenge ports.

## [0.5.1] - 2026-07-30

### Fixed

- GitHub Actions image publishing now builds the Java runner with the current project version instead of a stale release tag.

### Changed

- Production deployment defaults now target the Java file-submission UX release.

## [0.5.0] - 2026-07-30

### Added

- File-based submission workflow for JAVA_CODE exercises.
- Required per-exercise Java filename validation.
- Input/output examples on practice pages.
- Submitted source viewer and inline resubmission editor.
- Submission source metadata persistence for original filename and SHA-256.

### Changed

- JAVA_CODE exercises now keep the full statement, examples, submission and result in one workflow.
- Initial browser code textarea is replaced by local-file upload.

## [0.4.2] - 2026-07-30

### Fixed

- GitHub Actions checks now receive a PostgreSQL service.
- Production Compose and deployment scripts are aligned with GHCR release images.
- Server deployment pulls the Java runner image before accepting submissions.

### Added

- End-to-end server deployment verification and production smoke workflow.

## [0.4.1] - 2026-07-30

### Added

- Production OCI images for backend, frontend and Java runner.
- GitHub Actions CI and GHCR publishing workflows.
- Pull-only production Docker Compose deployment.
- Server preflight, update, rollback and PostgreSQL backup scripts.
- Image metadata, provenance and deployment smoke checks.

### Changed

- Java runner workspaces now support a containerized backend through a shared host path.
- Production frontend is served by Nginx rather than the Vite development server.

## [0.4.0] - 2026-07-30

### Added

- Ten complete automatically evaluated exercises.
- Docker-isolated Java code runner.
- TCP, UDP, RMI and SOAP challenge services.
- Submission and challenge-session history.
- Route-based practice and verdict pages.
- Downloadable network starter projects.
- Transactional development content importer.
- Content/database consistency verification.

### Changed

- Exercises now provide automatic platform grading instead of relying on manual self-checking.
- SOAP exercise now uses a real WSDL-based SOAP stack.

## [0.3.0] - 2026-07-30

### Added

- Spring Boot REST backend.
- PostgreSQL persistence with Flyway migrations.
- Exercise catalog API.
- Vue Router routes.
- API-driven exercise catalog and detail pages.
- Development PostgreSQL Compose file.
- Production Nginx reverse-proxy example.
- Backend tests and structured API errors.

### Changed

- PostgreSQL replaces generated static JSON as the frontend runtime data source.
- Vietnamese UI and pilot content now use proper diacritics and consistent language.
- Exercise navigation is route-based and reload-safe.

## [0.2.0] - 2026-07-30

### Added

- Build-time exercise content index.
- Exercise catalog loaded from JSON content.
- Search and metadata filters.
- Exercise detail view.
- Improved content validation.
- Content rendering checkpoint.

### Changed

- Frontend now uses real pilot exercise content instead of static status-only markup.

## [0.1.0] - 2026-07-30

### Added

- Repository bootstrap.
- Research snapshots.
- Initial content schema.
- Three draft pilot exercises.
- Minimal Vue/Vite frontend.
- Content validation script.
