package ir.co.sadad.paymentBill.dtos;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class PaymentVerificationResDto implements Serializable {

    private static final long serialVersionUID = -5359713919919447493L;
    private Long orderId;
    private String referenceNo;
    private String traceNo;
    private LocalDateTime transactionDate;
    private String description;
    private Integer statusCode;
    private String hashedCardNo;
    private String cardNo;


}
