package ir.co.sadad.paymentBill.dtos.ipg;

import lombok.Getter;
import lombok.Setter;

/**
 * request dto of ipg for payment request Token
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class IPGPaymentRequestReqDto {
    /**
     * userId which is extracted from oauth token
     */
    private String userId;

    /**
     * id of order in microservices
     */
    private String requestId;

    private Long amount;

    /**
     * which microservices calls this service
     */
    private String serviceType;

    /**
     * national id of card holder -  nullable
     */
    private String ssn;

    /**
     * id of device of user == serial Id
     */
    private String userDeviceId;

    private String invoiceNumber;

    private String paymentNumber;
}
