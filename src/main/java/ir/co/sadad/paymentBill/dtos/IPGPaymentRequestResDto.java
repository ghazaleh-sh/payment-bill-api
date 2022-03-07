package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * response of ipg for payment request token
 *
 * @author g.shahrokhabadi
 */
@Getter
@Setter
public class IPGPaymentRequestResDto implements Serializable {
    private static final long serialVersionUID = -2319886594765423453L;
    private String token;

    private String requestId;

//    private String paymentType;

    private String merchantId;

    private String terminalId;

}
