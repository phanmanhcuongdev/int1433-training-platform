FROM eclipse-temurin:21-jdk

RUN useradd --uid 10001 --create-home runner
WORKDIR /workspace
USER runner

CMD ["java", "-version"]
