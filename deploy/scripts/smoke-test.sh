#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
require_command curl
load_env

BASE_URL="${SMOKE_BASE_URL:-http://127.0.0.1:${WEB_HTTP_PORT:-80}}"
wait_http "$BASE_URL/healthz" 30
curl -fsS "$BASE_URL/" >/dev/null
curl -fsS "$BASE_URL/actuator/health" | grep -q '"status":"UP"'
curl -fsS "$BASE_URL/api/v1/exercises?size=20" | grep -q '"totalItems":10'
curl -fsS "$BASE_URL/ws/factorization.wsdl" | grep -Eq 'definitions|wsdl:definitions'
docker image inspect "$JAVA_RUNNER_IMAGE" >/dev/null

PARTICIPANT_ID="$(cat /proc/sys/kernel/random/uuid)"
REQUEST_FILE="$(mktemp)"
RESPONSE_FILE="$(mktemp)"
cat > "$REQUEST_FILE" <<'JSON'
{
  "language": "JAVA",
  "sourceCode": "import java.io.*; public class Main { public static void main(String[] args) throws Exception { BufferedReader in = new BufferedReader(new InputStreamReader(System.in, java.nio.charset.StandardCharsets.UTF_8)); BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, java.nio.charset.StandardCharsets.UTF_8)); String line = in.readLine(); if (line == null) line = \"\"; out.write(line.trim().replaceAll(\"\\\\s+\", \" \").toUpperCase(java.util.Locale.ROOT)); out.newLine(); out.flush(); } }"
}
JSON
curl -fsS -H "Content-Type: application/json" -H "X-Participant-Id: $PARTICIPANT_ID" \
  -d "@$REQUEST_FILE" "$BASE_URL/api/v1/exercises/fnd-character-flush-001/code-submissions" > "$RESPONSE_FILE"
SUBMISSION_ID="$(sed -n 's/.*"id":"\([^"]*\)".*/\1/p' "$RESPONSE_FILE")"
[ "$SUBMISSION_ID" ] || die "Unable to read submission id from smoke response."
for _ in $(seq 1 20); do
  STATUS="$(curl -fsS -H "X-Participant-Id: $PARTICIPANT_ID" "$BASE_URL/api/v1/submissions/$SUBMISSION_ID")"
  printf '%s' "$STATUS" | grep -q '"verdict":"AC"' && break
  sleep 1
done
curl -fsS -H "X-Participant-Id: $PARTICIPANT_ID" "$BASE_URL/api/v1/submissions/$SUBMISSION_ID" | grep -q '"verdict":"AC"'
docker ps --filter name=int1433-runner --format '{{.Names}}' | grep -q . && die "Orphan runner container detected."
if compose exec -T backend sh -c 'find "$JAVA_RUNNER_WORKSPACE_ROOT" -mindepth 1 -maxdepth 1 -name "submission-*" | grep -q .'; then
  die "Runner workspace leak detected."
fi
rm -f "$REQUEST_FILE" "$RESPONSE_FILE"
printf 'Smoke test passed against %s.\n' "$BASE_URL"
