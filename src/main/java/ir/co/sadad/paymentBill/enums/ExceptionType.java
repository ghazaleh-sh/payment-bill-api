package ir.co.sadad.paymentBill.enums;

public enum ExceptionType {

    IllegalArgumentCoddedException(400),
    DuplicateResourceCodedException(409),
    ResourceNotFoundCodedException(404),
    VerifyFailedCodedException(424),
    AuthorizationFailedCodedException(401),
    PaymentAPIConnectionException(500);
    private final int status;

    ExceptionType(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
