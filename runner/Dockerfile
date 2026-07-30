FROM eclipse-temurin:21-jdk
ARG VERSION=0.4.1
ARG REVISION=unknown
ARG CREATED=unknown
ARG SOURCE=unknown

LABEL org.opencontainers.image.title="INT1433 Java Runner" \
      org.opencontainers.image.description="Isolated Java 21 runner image for INT1433 code exercises" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.source="${SOURCE}" \
      org.opencontainers.image.revision="${REVISION}" \
      org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.created="${CREATED}"

RUN useradd --uid 10001 --create-home runner
WORKDIR /workspace
USER runner

CMD ["java", "-version"]
