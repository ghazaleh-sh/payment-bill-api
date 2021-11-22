package ir.co.sadad.paymentBill.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends BaseException {

    public InternalServerException(String messageCode) {
        this.code = messageCode;
    }

    @Override
    public String getErrorCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

