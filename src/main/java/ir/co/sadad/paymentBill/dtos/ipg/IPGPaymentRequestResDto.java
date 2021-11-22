package ir.co.sadad.paymentBill.dtos.ipg;

import lombok.Getter;
import lombok.Setter;

/**
 * response of ipg for payment request token
 *
 * @author g.shahrokhabadi
 */
@Getter
@Setter
public class IPGPaymentRequestResDto {
    private String token;

    private String requestId;

//    private String paymentType;

    private String merchantId;

    private String terminalId;

}
