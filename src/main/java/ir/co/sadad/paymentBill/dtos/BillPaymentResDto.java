package ir.co.sadad.paymentBill.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * using for response of bill request after getting token through psp.
 * using for request of verify method old version
 *
 * @author g.shahrokhabadi
 */
@Data
public class BillPaymentResDto implements Serializable {
    private static final long serialVersionUID = 7226858611983761002L;

    @Schema(title = "توکن دریافتی از psp")
    private String token;

    @Schema(title = "شماره سفارش قبض")
    private String orderId;

}
