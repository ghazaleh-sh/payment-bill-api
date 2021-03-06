package ir.co.sadad.paymentBill.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BillPaymentException extends RuntimeException {

    private static final long serialVersionUID = -3198038928433281102L;
    private final HttpStatus httpStatus;
    private Integer code;
    private GlobalErrorResponse globalErrorResponse;

    public BillPaymentException(String message, Integer code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BillPaymentException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BillPaymentException(GlobalErrorResponse globalErrorResponse, HttpStatus httpStatus) {
        this.globalErrorResponse = globalErrorResponse;
        this.httpStatus = httpStatus;
    }

}