package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * using for response of bill request after getting token through psp.
 * using for request of verify method old version
 *
 * @author g.shahrokhabadi
 */
@Setter
@Getter
public class InvoiceVerifyReqDto {

    private String token;
    private String orderId;

}
