#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
load_env
[ "${1:-}" ] || die "Usage: deploy/scripts/rollback.sh <previous-version>"

PREVIOUS_VERSION="$1"
export APP_VERSION="$PREVIOUS_VERSION"
export JAVA_RUNNER_IMAGE="${GHCR_REGISTRY:-ghcr.io}/${GHCR_OWNER}/int1433-java-runner:${PREVIOUS_VERSION}"

printf 'Rolling application images back to %s.\n' "$PREVIOUS_VERSION"
printf 'Warning: Flyway migrations may make application rollback incompatible. Database migrations are not rolled back destructively.\n'
docker pull "$(image_ref int1433-backend)"
docker pull "$(image_ref int1433-web)"
docker pull "$JAVA_RUNNER_IMAGE"
compose up -d
"$SCRIPT_DIR/smoke-test.sh"
