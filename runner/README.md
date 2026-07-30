# Java Runner

Runner image for `JAVA_CODE` exercises.

The Spring Boot API invokes Docker with this image. Submitted code is compiled and executed inside a short-lived container with:

- Java 21 only.
- No outbound network.
- Memory, CPU, PID and wall-clock limits.
- Ephemeral workspace.
- No dependency download.

The Docker socket is not mounted into the runner container.

The backend's access to the Docker daemon is trusted infrastructure. Do not expose Docker daemon access to end users, and do not run submitted code in the Spring Boot JVM or directly on the host.
