package ir.co.sadad.paymentBill.dtos.payment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PaymentVerificationResponse {
    private Long orderId;
    private String referenceNo;
    private String traceNo;
    private LocalDateTime transactionDate;
    private String description;
    private Integer statusCode;
    private String hashedCardNo;
    private String cardNo;

    public PaymentVerificationResponse() {
    }

    public PaymentVerificationResponse(Long orderId, String referenceNo, String traceNo, LocalDateTime transactionDate, String description, Integer statusCode,String hashedCardNo, String cardNo) {
        this.orderId = orderId;
        this.referenceNo = referenceNo;
        this.traceNo = traceNo;
        this.transactionDate = transactionDate;
        this.description = description;
        this.statusCode = statusCode;
        this.cardNo = cardNo;
        this.hashedCardNo = hashedCardNo;
    }

}
