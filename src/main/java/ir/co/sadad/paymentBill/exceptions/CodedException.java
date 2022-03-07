package ir.co.sadad.paymentBill.exceptions;

import ir.co.sadad.paymentBill.enums.ExceptionType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CodedException extends RuntimeException {

    private static final long serialVersionUID = 7444389415841710379L;
    private transient int status;
    private String code;
    private String errorSummary;
    private List<String> messages;


    protected CodedException(int status, String code, String errorSummary, String... messages) {
        super(errorSummary);
        this.status = status;
        this.errorSummary = errorSummary;
        this.code = code;
        this.messages = new ArrayList<>();

        for (String message : messages) {
            this.messages.add(message);
        }
    }

    public CodedException(ExceptionType exceptionType, String code, String errorSummary, String... messages){
        super(errorSummary);
        this.status = exceptionType.getStatus();
        this.errorSummary = errorSummary;
        this.code = code;
        this.messages = new ArrayList<>();

        for (String message : messages) {
            this.messages.add(message);
        }
    }

}
