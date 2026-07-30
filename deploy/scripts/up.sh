#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
require_command curl
load_env

mkdir -p "$JAVA_RUNNER_WORKSPACE_ROOT"
chmod 1777 "$JAVA_RUNNER_WORKSPACE_ROOT"
compose up -d
wait_http "http://127.0.0.1:${WEB_HTTP_PORT:-80}/healthz" 45
compose ps
