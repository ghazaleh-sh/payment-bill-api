package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * dto for request verification ipg service
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class IPGVerifyReqDto {

    private String userId;

    /**
     * equivalent with orderId that retrieved by psp-payment-response
     */
    private String requestId;

    /**
     * token of psp-payment
     */
    private String token;

    /**
     * here is BILL_PAYMENT
     */
    private String serviceType;

}
