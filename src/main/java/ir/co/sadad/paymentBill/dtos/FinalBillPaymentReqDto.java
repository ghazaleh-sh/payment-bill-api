package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * this dto will come from body request of ipg service
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class FinalBillPaymentReqDto {

    private String requestId;
    private String token;
    /**
     * extracted from oauth token when calling ipg-payment-request service and saved in transaction table
     */
    private String userId;
    private String primaryAccNo;
    private String userDeviceId;

}
