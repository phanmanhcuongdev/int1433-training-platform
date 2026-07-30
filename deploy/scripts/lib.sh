#!/usr/bin/env bash
set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd -- "$SCRIPT_DIR/../.." && pwd)"
COMPOSE_FILE="$REPO_ROOT/deploy/docker-compose.prod.yml"
ENV_FILE="${INT1433_ENV_FILE:-$REPO_ROOT/deploy/.env.prod}"

die() {
  printf 'ERROR: %s\n' "$*" >&2
  exit 1
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || die "Missing required command: $1"
}

load_env() {
  [ -f "$ENV_FILE" ] || die "Missing $ENV_FILE. Copy deploy/.env.prod.example to deploy/.env.prod first."
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
  : "${APP_VERSION:?APP_VERSION is required}"
  : "${GHCR_OWNER:?GHCR_OWNER is required}"
  : "${POSTGRES_DB:?POSTGRES_DB is required}"
  : "${POSTGRES_USER:?POSTGRES_USER is required}"
  : "${POSTGRES_PASSWORD:?POSTGRES_PASSWORD is required}"
  : "${CHALLENGE_PUBLIC_HOST:?CHALLENGE_PUBLIC_HOST is required}"
  : "${RMI_PUBLIC_HOST:?RMI_PUBLIC_HOST is required}"
  : "${JAVA_RUNNER_IMAGE:?JAVA_RUNNER_IMAGE is required}"
  : "${JAVA_RUNNER_WORKSPACE_ROOT:?JAVA_RUNNER_WORKSPACE_ROOT is required}"
  : "${DOCKER_GID:?DOCKER_GID is required}"
}

compose() {
  docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" "$@"
}

image_ref() {
  local name="$1"
  printf '%s/%s/%s:%s' "${GHCR_REGISTRY:-ghcr.io}" "$GHCR_OWNER" "$name" "$APP_VERSION"
}

wait_http() {
  local url="$1"
  local attempts="${2:-30}"
  for _ in $(seq 1 "$attempts"); do
    if curl -fsS "$url" >/dev/null 2>&1; then
      return 0
    fi
    sleep 2
  done
  die "Timed out waiting for $url"
}
