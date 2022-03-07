package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * dto for request verification ipg service
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class IPGVerifyReqDto implements Serializable {
    private static final long serialVersionUID = -991129889563703351L;

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
