package vn.edu.ptit.int1433.training.challenge.soap;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class SoapProtocolException extends RuntimeException {
    public SoapProtocolException(String message) {
        super(message);
    }
}
