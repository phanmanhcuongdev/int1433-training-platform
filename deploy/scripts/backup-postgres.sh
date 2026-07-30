#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
require_command gzip
load_env

BACKUP_DIR="${BACKUP_DIR:-$REPO_ROOT/deploy/backups}"
mkdir -p "$BACKUP_DIR"
OUTPUT="$BACKUP_DIR/int1433-$(date -u +%Y%m%dT%H%M%SZ).sql.gz"
compose exec -T postgres pg_dump -U "$POSTGRES_USER" "$POSTGRES_DB" | gzip -9 > "$OUTPUT"
chmod 600 "$OUTPUT"
printf 'Created backup: %s\n' "$OUTPUT"
