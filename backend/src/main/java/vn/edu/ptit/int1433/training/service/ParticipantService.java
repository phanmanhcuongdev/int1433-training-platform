package vn.edu.ptit.int1433.training.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.edu.ptit.int1433.training.exception.ParticipantException;

@Service
public class ParticipantService {
    public static final String HEADER = "X-Participant-Id";

    public UUID parse(String value) {
        if (!StringUtils.hasText(value)) {
            throw new ParticipantException("Missing X-Participant-Id header");
        }
        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException exception) {
            throw new ParticipantException("X-Participant-Id must be a valid UUID");
        }
    }
}
