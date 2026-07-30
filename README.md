# INT1433 Training Platform

Unofficial training platform for INT1433 - Lap trinh mang.

This project is not an official PTIT exam system, not a replacement for course announcements, and not a source of current exam server IPs, ports, or credentials.

## Goal

Build a Java-first practice platform for INT1433. The project starts as a content-only site and may later grow into a local mock judge after the content schema and pilot exercises are reviewed.

## Current Status

- Version: `0.1.0`
- State: bootstrap
- Frontend: minimal Vue 3/Vite static app
- Content: 3 draft pilot exercises
- Judge: not implemented
- Backend/database/auth: not implemented

## Tracks

- Exam Track: Java-first, close to TCP/UDP/RMI/SOAP Web Service exam contracts.
- Extended Networking Track: TCP/UDP and networking concepts beyond exam drills.
- Backend/Distributed Systems Track: later backend/distributed systems extensions, not part of default INT1433 mock exams.

## Stack

- Vue 3 + Vite + JavaScript for Phase 1 content-only UI.
- JSON content files under `content/exercises`.
- Node.js validation script with no runtime dependencies.

## Repository Structure

```text
docs/       Research snapshots, architecture notes, ADRs, checkpoints.
content/    Exercise schema, draft exercises, future mock exam content.
scripts/    Content validation scripts.
web/        Minimal static Vue/Vite frontend.
```

## Commands

Validate content without installing frontend dependencies:

```bash
npm run validate
```

Run the frontend:

```bash
npm install
npm run dev
```

Build the frontend:

```bash
npm run build
```

## Roadmap

1. Phase 0: bootstrap, schema, 3 draft pilot exercises.
2. Phase 1A: static site renders content and grows to 12 reviewed exercises.
3. Phase 1B: grow to 24 exercises.
4. Phase 2: local mock judge pilot.
5. Phase 3: online judge only after security review.

## Research

Initial research snapshots are stored in:

- [docs/research/exam](docs/research/exam)
- [docs/research/platform](docs/research/platform)

These are synthesized notes, not official PTIT documents.

