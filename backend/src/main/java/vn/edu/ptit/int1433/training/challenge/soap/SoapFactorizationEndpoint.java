package vn.edu.ptit.int1433.training.challenge.soap;

import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.repository.ChallengeSessionRepository;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Endpoint
public class SoapFactorizationEndpoint {
    public static final String NAMESPACE = "http://training.int1433.ptit.edu.vn/ws/factorization";

    private final ChallengeSessionRepository repository;
    private final ChallengeResultService resultService;

    public SoapFactorizationEndpoint(ChallengeSessionRepository repository, ChallengeResultService resultService) {
        this.repository = repository;
        this.resultService = resultService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "request")
    @ResponsePayload
    public DOMSource request(@RequestPayload Element request) {
        String token = childText(request, "token");
        String qCode = childText(request, "qCode");
        var session = validateSession(token, qCode);
        resultService.trace(session.getId(), ChallengeState.REQUEST_ACCEPTED.name(), "SOAP request hợp lệ.");
        resultService.trace(session.getId(), ChallengeState.RESPONSE_SENT.name(), "SOAP đã trả dữ liệu.");
        return response("requestResponse", "n", String.valueOf(session.getPayload().get("n")));
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "submit")
    @ResponsePayload
    public DOMSource submit(@RequestPayload Element submit) {
        String token = childText(submit, "token");
        String qCode = childText(submit, "qCode");
        var session = validateSession(token, qCode);
        List<Integer> submitted = childInts(submit, "factors");
        @SuppressWarnings("unchecked")
        List<Integer> expected = (List<Integer>) session.getExpectedAnswer().get("factors");
        boolean accepted = expected.equals(submitted);
        if (accepted) {
            resultService.verdict(session.getId(), ChallengeState.AC, Verdict.AC, "AC", "Kết quả đúng.");
        } else {
            resultService.verdict(session.getId(), ChallengeState.WA, Verdict.WA, "WRONG_ANSWER", "Danh sách thừa số sai.");
        }
        return response("submitResponse", "accepted", String.valueOf(accepted));
    }

    private vn.edu.ptit.int1433.training.entity.ChallengeSession validateSession(String token, String qCode) {
        var session = repository.findByQCode(qCode).orElseThrow(() -> new SoapProtocolException("Token hoặc qCode SOAP không hợp lệ."));
        if (!"ws-data-factorization-001".equals(session.getExercise().getId()) || !session.getTokenHash().equals(sha256(token))) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_TOKEN_OR_QCODE", "Token hoặc qCode SOAP không hợp lệ.");
            throw new SoapProtocolException("Token hoặc qCode SOAP không hợp lệ.");
        }
        if (session.getExpiresAt().isBefore(OffsetDateTime.now())) {
            resultService.verdict(session.getId(), ChallengeState.EXPIRED, Verdict.EXPIRED, "SESSION_EXPIRED", "Phiên SOAP đã hết hạn.");
            throw new SoapProtocolException("Phiên SOAP đã hết hạn.");
        }
        return session;
    }

    private String childText(Element root, String localName) {
        var nodes = root.getElementsByTagNameNS("*", localName);
        if (nodes.getLength() == 0) {
            throw new SoapProtocolException("Thiếu trường SOAP: " + localName);
        }
        return nodes.item(0).getTextContent().trim();
    }

    private List<Integer> childInts(Element root, String localName) {
        var nodes = root.getElementsByTagNameNS("*", localName);
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i += 1) {
            try {
                values.add(Integer.parseInt(nodes.item(i).getTextContent().trim()));
            } catch (NumberFormatException exception) {
                throw new SoapProtocolException("Danh sách factors không hợp lệ.");
            }
        }
        return values;
    }

    private DOMSource response(String rootName, String childName, String value) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElementNS(NAMESPACE, rootName);
            Element child = document.createElementNS(NAMESPACE, childName);
            child.setTextContent(value);
            root.appendChild(child);
            document.appendChild(root);
            return new DOMSource(root);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to build SOAP response", exception);
        }
    }

    private String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash SOAP token", exception);
        }
    }
}
