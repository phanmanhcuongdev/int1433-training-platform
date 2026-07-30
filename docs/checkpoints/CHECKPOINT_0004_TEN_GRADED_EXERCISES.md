# Checkpoint 0004: Ten Graded Exercises

Date: 2026-07-30

Version: `0.4.0`

Commit/tag: release commit and `v0.4.0` after final checks pass.

## Exercises

| Exercise | Mode |
| --- | --- |
| `fnd-character-flush-001` | `JAVA_CODE` |
| `fnd-data-order-001` | `JAVA_CODE` |
| `tcp-byte-prime-sum-001` | `NETWORK_CHALLENGE` |
| `tcp-data-gcd-lcm-001` | `NETWORK_CHALLENGE` |
| `tcp-character-normalize-001` | `NETWORK_CHALLENGE` |
| `tcp-object-product-001` | `NETWORK_CHALLENGE` |
| `udp-string-request-id-001` | `NETWORK_CHALLENGE` |
| `udp-object-product-001` | `NETWORK_CHALLENGE` |
| `rmi-data-pythagorean-001` | `NETWORK_CHALLENGE` |
| `ws-data-factorization-001` | `NETWORK_CHALLENGE` |

## Runtime Topology

- Frontend: `http://localhost:5173`
- Backend/API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`
- TCP challenge range: `19000-19020` by default
- UDP challenge range: `19100-19120` by default
- RMI registry: `19200` by default
- SOAP WSDL: `/ws/factorization.wsdl`

## Commands

```bash
docker compose -f deploy/docker-compose.dev.yml up -d
docker build -t int1433-java-runner:0.4.0 runner
npm run content:import:dry
npm run content:import
npm run content:check
npm run starters:check
npm run frontend:build
./backend/mvnw test
npm run check
```

## Verification Matrix

Backend tests cover Java runner verdicts, real TCP/UDP socket/datagram flows, RMI request/submit, SOAP WSDL/request/submit, importer behavior, catalog APIs and migrations.

| Area | Status |
| --- | --- |
| Java runner AC/WA/CE/RE/TLE/output cap/network blocked | Passing in backend tests |
| TCP/UDP/RMI/SOAP challenge AC and failure paths | Passing in backend tests |
| Content/DB consistency | `npm run content:check` |
| Starter compilation | `npm run starters:check` |
| Frontend build | `npm run frontend:build` |

## Security Boundaries

- Submitted Java runs only in Docker runner containers.
- The Spring Boot JVM does not execute submitted source.
- Runner uses no outbound network, resource limits, output caps and cleanup.
- Docker daemon access is trusted infrastructure and must not be exposed to users.
- Anonymous participant UUID is not authentication.

## Known Limitations

- No real account authentication.
- No production deployment automation.
- No leaderboard or classroom features.
- Browser verification remains lightweight; no heavy E2E browser framework is included.
