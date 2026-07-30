#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

"$SCRIPT_DIR/backup-postgres.sh"
"$SCRIPT_DIR/pull.sh"
"$SCRIPT_DIR/up.sh"
"$SCRIPT_DIR/smoke-test.sh"
printf 'Update completed. Old images were not deleted automatically.\n'
