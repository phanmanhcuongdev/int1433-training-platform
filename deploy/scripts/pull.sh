#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
load_env

docker pull "$(image_ref int1433-backend)"
docker pull "$(image_ref int1433-web)"
docker pull "$JAVA_RUNNER_IMAGE"
compose pull postgres
printf 'Pulled application and PostgreSQL images for %s.\n' "$APP_VERSION"
