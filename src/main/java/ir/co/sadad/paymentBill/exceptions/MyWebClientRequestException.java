package ir.co.sadad.paymentBill.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
public class MyWebClientRequestException extends BaseException {

    public MyWebClientRequestException(String messageCode) {
        this.code = messageCode;
    }

    @Override
    public String getErrorCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.REQUEST_TIMEOUT;
    }
}