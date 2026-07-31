ARG MAVEN_IMAGE=maven:3.9.9-eclipse-temurin-21
ARG RUNTIME_IMAGE=eclipse-temurin:21-jre-alpine

FROM ${MAVEN_IMAGE} AS build
WORKDIR /workspace
COPY backend/pom.xml ./pom.xml
RUN mvn -B -q -DskipTests dependency:go-offline
COPY backend/src ./src
RUN mvn -B -q -DskipTests package

FROM ${RUNTIME_IMAGE} AS runtime
ARG VERSION=0.6.0
ARG REVISION=unknown
ARG CREATED=unknown
ARG SOURCE=unknown

LABEL org.opencontainers.image.title="INT1433 Training Platform Backend" \
      org.opencontainers.image.description="Spring Boot API and challenge engine for INT1433 Training Platform" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.source="${SOURCE}" \
      org.opencontainers.image.revision="${REVISION}" \
      org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.created="${CREATED}"

RUN apk add --no-cache docker-cli curl \
    && addgroup -S int1433 \
    && adduser -S -G int1433 -u 10001 int1433 \
    && mkdir -p /app /var/lib/int1433/runner-workspaces \
    && chmod 1777 /var/lib/int1433/runner-workspaces

WORKDIR /app
COPY --from=build /workspace/target/training-platform-*.jar /app/app.jar

ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_RUNNER_WORKSPACE_ROOT=/var/lib/int1433/runner-workspaces \
    JAVA_RUNNER_IMAGE=int1433-java-runner:0.6.0

EXPOSE 8080 19000-19020 19100-19120/udp 19200
USER int1433
HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=5 CMD curl -fsS http://127.0.0.1:${SERVER_PORT}/actuator/health >/dev/null || exit 1
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
