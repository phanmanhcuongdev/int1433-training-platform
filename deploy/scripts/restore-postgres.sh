#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
require_command gzip
load_env

[ "${1:-}" ] || die "Usage: deploy/scripts/restore-postgres.sh <backup.sql.gz>"
[ -f "$1" ] || die "Backup file not found: $1"
printf 'Restoring %s into database %s. Press Ctrl+C within 5 seconds to cancel.\n' "$1" "$POSTGRES_DB"
sleep 5
gzip -dc "$1" | compose exec -T postgres psql -U "$POSTGRES_USER" "$POSTGRES_DB"
