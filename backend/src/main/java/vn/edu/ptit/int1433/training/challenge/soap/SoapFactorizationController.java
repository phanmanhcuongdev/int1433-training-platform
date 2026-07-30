package vn.edu.ptit.int1433.training.challenge.soap;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.repository.ChallengeSessionRepository;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@RestController
@RequestMapping("/ws")
public class SoapFactorizationController {
    private final ChallengeSessionRepository repository;
    private final ChallengeResultService resultService;

    public SoapFactorizationController(ChallengeSessionRepository repository, ChallengeResultService resultService) {
        this.repository = repository;
        this.resultService = resultService;
    }

    @GetMapping(value = "/factorization.wsdl", produces = MediaType.TEXT_XML_VALUE)
    public String wsdl() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <definitions name="DataService" targetNamespace="http://training.int1433.ptit.edu.vn/ws"
              xmlns="http://schemas.xmlsoap.org/wsdl/"
              xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
              xmlns:tns="http://training.int1433.ptit.edu.vn/ws"
              xmlns:xsd="http://www.w3.org/2001/XMLSchema">
              <message name="requestInput"><part name="token" type="xsd:string"/><part name="qCode" type="xsd:string"/></message>
              <message name="requestOutput"><part name="n" type="xsd:int"/></message>
              <message name="submitInput"><part name="token" type="xsd:string"/><part name="qCode" type="xsd:string"/><part name="factors" type="xsd:string"/></message>
              <message name="submitOutput"><part name="accepted" type="xsd:boolean"/></message>
              <portType name="DataServicePortType">
                <operation name="request"><input message="tns:requestInput"/><output message="tns:requestOutput"/></operation>
                <operation name="submit"><input message="tns:submitInput"/><output message="tns:submitOutput"/></operation>
              </portType>
              <binding name="DataServiceBinding" type="tns:DataServicePortType">
                <soap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http"/>
                <operation name="request"><soap:operation soapAction="request"/></operation>
                <operation name="submit"><soap:operation soapAction="submit"/></operation>
              </binding>
              <service name="DataService"><port name="DataServicePort" binding="tns:DataServiceBinding"><soap:address location="/ws/factorization"/></port></service>
            </definitions>
            """;
    }

    @PostMapping(value = "/factorization", consumes = {MediaType.TEXT_XML_VALUE, "application/soap+xml"}, produces = MediaType.TEXT_XML_VALUE)
    public String soap(@RequestBody String body) throws Exception {
        String token = tag(body, "token");
        String qCode = tag(body, "qCode");
        var session = repository.findByQCode(qCode).orElseThrow();
        if (!session.getTokenHash().equals(sha256(token)) || !"ws-data-factorization-001".equals(session.getExercise().getId())) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_TOKEN_OR_QCODE", "Token hoặc qCode SOAP không hợp lệ.");
            return envelope("<accepted>false</accepted>");
        }
        if (body.contains("<request")) {
            resultService.trace(session.getId(), ChallengeState.REQUEST_ACCEPTED.name(), "SOAP request hợp lệ.");
            resultService.trace(session.getId(), ChallengeState.RESPONSE_SENT.name(), "SOAP đã trả dữ liệu.");
            return envelope("<n>" + session.getPayload().get("n") + "</n>");
        }
        if (body.contains("<submit")) {
            String factors = tag(body, "factors").replaceAll("\\s+", "");
            boolean ok = "2,2,2,3,3,5".equals(factors);
            if (ok) {
                resultService.verdict(session.getId(), ChallengeState.AC, Verdict.AC, "AC", "Kết quả đúng.");
            } else {
                resultService.verdict(session.getId(), ChallengeState.WA, Verdict.WA, "WRONG_ANSWER", "Danh sách thừa số sai.");
            }
            return envelope("<accepted>" + ok + "</accepted>");
        }
        resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_SOAP_OPERATION", "SOAP operation không hợp lệ.");
        return envelope("<accepted>false</accepted>");
    }

    private String tag(String body, String tag) {
        Matcher matcher = Pattern.compile("<(?:\\w+:)?" + tag + ">(.*?)</(?:\\w+:)?" + tag + ">", Pattern.DOTALL).matcher(body);
        if (!matcher.find()) {
            return "";
        }
        return matcher.group(1).trim();
    }

    private String envelope(String inner) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>" + inner + "</soap:Body></soap:Envelope>";
    }

    private String sha256(String value) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }
}
