package ir.co.sadad.paymentBill.exceptions;

import ir.co.sadad.paymentBill.enums.ExceptionType;

import java.util.ArrayList;
import java.util.List;

public class CodedException extends RuntimeException {

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

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getErrorSummary() {
        return errorSummary;
    }

    public List<String> getMessages() {
        return messages;
    }

}
