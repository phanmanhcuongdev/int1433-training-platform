#!/usr/bin/env bash
set -Eeuo pipefail
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_command docker
require_command curl
load_env

docker compose version >/dev/null || die "Docker Compose plugin is not available."
[ -S /var/run/docker.sock ] || die "Docker socket /var/run/docker.sock is missing."

[ "$GHCR_OWNER" != "replace-with-lowercase-owner" ] || die "Set GHCR_OWNER in deploy/.env.prod."
[ "$POSTGRES_PASSWORD" != "replace-with-long-random-secret" ] || die "Set a real POSTGRES_PASSWORD. Example: openssl rand -base64 36"
[ "$DOCKER_GID" != "replace-with-docker-socket-gid" ] || die "Set DOCKER_GID in deploy/.env.prod. Start with: stat -c '%g' /var/run/docker.sock"
ACTUAL_DOCKER_GID="$(stat -c '%g' /var/run/docker.sock || true)"
if [ "$DOCKER_GID" != "$ACTUAL_DOCKER_GID" ]; then
  printf 'Warning: DOCKER_GID=%s differs from host stat group %s. This can be valid on idmapped/rootless hosts; confirm backend can run `docker image inspect`.\n' "$DOCKER_GID" "$ACTUAL_DOCKER_GID"
fi
case "$CHALLENGE_PUBLIC_HOST" in
  localhost|127.*|0.0.0.0|backend|web|replace-with-server-ip-or-domain) die "CHALLENGE_PUBLIC_HOST must be a real public hostname or IP." ;;
esac
case "$RMI_PUBLIC_HOST" in
  localhost|127.*|0.0.0.0|backend|web|replace-with-server-ip-or-domain) die "RMI_PUBLIC_HOST must be a real public hostname or IP." ;;
esac

mkdir -p "$JAVA_RUNNER_WORKSPACE_ROOT"
chmod 1777 "$JAVA_RUNNER_WORKSPACE_ROOT"
[ -w "$JAVA_RUNNER_WORKSPACE_ROOT" ] || die "Runner workspace is not writable: $JAVA_RUNNER_WORKSPACE_ROOT"

printf 'Docker: %s\n' "$(docker --version)"
printf 'Compose: %s\n' "$(docker compose version)"
printf 'CPU cores: %s\n' "$(getconf _NPROCESSORS_ONLN)"
printf 'Memory:\n'
free -h || true
printf 'Disk:\n'
df -h "$REPO_ROOT" "$JAVA_RUNNER_WORKSPACE_ROOT" || true

compose_owns_tcp_port() {
  local port="$1"
  local containers
  containers="$(compose ps -q 2>/dev/null || true)"
  [ -n "$containers" ] || return 1
  docker inspect --format '{{ range $private, $bindings := .NetworkSettings.Ports }}{{ range $binding := $bindings }}{{ $binding.HostPort }} {{ end }}{{ end }}' $containers 2>/dev/null |
    tr ' ' '\n' |
    grep -qx "$port"
}

for port in "${WEB_HTTP_PORT:-80}" "${CHALLENGE_TCP_PORT_MIN:-19000}" "${CHALLENGE_TCP_PORT_MAX:-19020}" "${RMI_REGISTRY_PORT:-19200}"; do
  if command -v ss >/dev/null 2>&1 && ss -ltn "( sport = :$port )" | grep -q ":$port"; then
    if compose_owns_tcp_port "$port"; then
      printf 'TCP port %s is already used by this Compose project; continuing for in-place update.\n' "$port"
    else
      die "TCP port $port is already in use by another process."
    fi
  fi
done

compose config >/dev/null
docker image inspect "$JAVA_RUNNER_IMAGE" >/dev/null 2>&1 || printf 'Runner image not present locally yet: %s\n' "$JAVA_RUNNER_IMAGE"
printf 'Preflight passed for APP_VERSION=%s.\n' "$APP_VERSION"
