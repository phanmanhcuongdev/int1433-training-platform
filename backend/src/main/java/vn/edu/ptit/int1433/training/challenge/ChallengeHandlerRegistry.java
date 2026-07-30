package vn.edu.ptit.int1433.training.challenge;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ChallengeHandlerRegistry {
    private final Map<String, ChallengeHandler> handlers;

    public ChallengeHandlerRegistry(List<ChallengeHandler> handlers) {
        this.handlers = handlers.stream().collect(Collectors.toMap(ChallengeHandler::graderKey, Function.identity()));
    }

    public ChallengeHandler get(String graderKey) {
        ChallengeHandler handler = handlers.get(graderKey);
        if (handler == null) {
            throw new IllegalStateException("No challenge handler registered for " + graderKey);
        }
        return handler;
    }
}
