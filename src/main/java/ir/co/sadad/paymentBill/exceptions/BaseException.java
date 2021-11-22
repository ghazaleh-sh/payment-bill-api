package ir.co.sadad.paymentBill.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.Map;

@Getter
public abstract class BaseException extends RuntimeException {

    protected String code;
    private HttpStatus httpStatusCode;
    private Map<String, Object> messageArgs;

    public BaseException() {
        httpStatusCode = HttpStatus.BAD_REQUEST;
    }

    protected BaseException(Throwable cause) {
        super(cause);
        httpStatusCode = HttpStatus.BAD_REQUEST;
    }

    public BaseException(String message) {
        super(message);
        httpStatusCode = HttpStatus.BAD_REQUEST;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        httpStatusCode = HttpStatus.BAD_REQUEST;
    }

    public abstract String getErrorCode();

}