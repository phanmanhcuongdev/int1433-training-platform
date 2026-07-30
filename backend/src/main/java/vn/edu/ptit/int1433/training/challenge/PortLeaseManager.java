package vn.edu.ptit.int1433.training.challenge;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class PortLeaseManager {
    private final ChallengeProperties properties;
    private final Set<Integer> leased = new HashSet<>();

    public PortLeaseManager(ChallengeProperties properties) {
        this.properties = properties;
        validateRange(properties.tcpPortMin(), properties.tcpPortMax(), "TCP");
        validateRange(properties.udpPortMin(), properties.udpPortMax(), "UDP");
    }

    public synchronized Lease leaseTcp() {
        for (int port = properties.tcpPortMin(); port <= properties.tcpPortMax(); port += 1) {
            if (!leased.contains(port) && tcpAvailable(port)) {
                leased.add(port);
                int leasedPort = port;
                return new Lease(leasedPort, () -> release(leasedPort));
            }
        }
        throw new PortUnavailableException("No TCP challenge port is available");
    }

    public synchronized Lease leaseUdp() {
        for (int port = properties.udpPortMin(); port <= properties.udpPortMax(); port += 1) {
            if (!leased.contains(port) && udpAvailable(port)) {
                leased.add(port);
                int leasedPort = port;
                return new Lease(leasedPort, () -> release(leasedPort));
            }
        }
        throw new PortUnavailableException("No UDP challenge port is available");
    }

    private synchronized void release(int port) {
        leased.remove(port);
    }

    private boolean tcpAvailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    private boolean udpAvailable(int port) {
        try (DatagramSocket ignored = new DatagramSocket(port)) {
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    private void validateRange(int min, int max, String name) {
        if (min <= 0 || max < min) {
            throw new IllegalStateException(name + " challenge port range is invalid");
        }
    }

    public record Lease(int port, Runnable release) implements AutoCloseable {
        @Override
        public void close() {
            release.run();
        }
    }
}
