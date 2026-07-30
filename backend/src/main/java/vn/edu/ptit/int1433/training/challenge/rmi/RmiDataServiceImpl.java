package vn.edu.ptit.int1433.training.challenge.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.contract.rmi.DataService;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.repository.ChallengeSessionRepository;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class RmiDataServiceImpl extends UnicastRemoteObject implements DataService {
    private final ChallengeSessionRepository repository;
    private final ChallengeResultService resultService;

    public RmiDataServiceImpl(ChallengeSessionRepository repository, ChallengeResultService resultService) throws RemoteException {
        super();
        this.repository = repository;
        this.resultService = resultService;
    }

    @Override
    public int[] request(String token, String qCode) throws RemoteException {
        var session = repository.findByQCode(qCode).orElseThrow(() -> new RemoteException("Invalid qCode"));
        if (!session.getTokenHash().equals(sha256(token)) || !"rmi-data-pythagorean-001".equals(session.getExercise().getId())) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_TOKEN_OR_QCODE", "Token hoặc qCode RMI không hợp lệ.");
            throw new RemoteException("Invalid token or qCode");
        }
        resultService.trace(session.getId(), ChallengeState.REQUEST_ACCEPTED.name(), "RMI request hợp lệ.");
        @SuppressWarnings("unchecked")
        List<Integer> values = (List<Integer>) session.getPayload().get("values");
        resultService.trace(session.getId(), ChallengeState.RESPONSE_SENT.name(), "RMI đã trả dữ liệu.");
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public boolean submit(String token, String qCode, int[][] triples) throws RemoteException {
        var session = repository.findByQCode(qCode).orElseThrow(() -> new RemoteException("Invalid qCode"));
        if (!session.getTokenHash().equals(sha256(token)) || !"rmi-data-pythagorean-001".equals(session.getExercise().getId())) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_TOKEN_OR_QCODE", "Token hoặc qCode RMI không hợp lệ.");
            throw new RemoteException("Invalid token or qCode");
        }
        boolean ok = triples.length == 1 && triples[0].length == 3 && triples[0][0] == 3 && triples[0][1] == 4 && triples[0][2] == 5;
        if (ok) {
            resultService.verdict(session.getId(), ChallengeState.AC, Verdict.AC, "AC", "Kết quả đúng.");
        } else {
            resultService.verdict(session.getId(), ChallengeState.WA, Verdict.WA, "WRONG_ANSWER", "Danh sách bộ ba Pythagore sai.");
        }
        return ok;
    }

    private String sha256(String value) throws RemoteException {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new RemoteException("SHA-256 unavailable", exception);
        }
    }
}
