package ir.co.sadad.paymentBill.exceptions;

public class TokenGenerationException extends Exception {

    private String resCode;
    private String message;

    public TokenGenerationException(String message, String resCode) {
        super(message);
        this.resCode = resCode;
        this.message = message;
    }

    public String getResCode() {
        return resCode;
    }
}
