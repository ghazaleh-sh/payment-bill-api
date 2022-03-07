package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * request dto of ipg for payment request Token
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class IPGPaymentRequestReqDto implements Serializable {
    private static final long serialVersionUID = 1413114498875202085L;
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
