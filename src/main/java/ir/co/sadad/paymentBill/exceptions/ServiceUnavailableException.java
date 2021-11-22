package ir.co.sadad.paymentBill.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public final class ServiceUnavailableException extends BaseException {

    public ServiceUnavailableException(String messageCode) {
        this.code = messageCode;
    }

    @Override
    public String getErrorCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
