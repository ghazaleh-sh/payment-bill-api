package ir.co.sadad.paymentBill.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * dto for first request of payment- using by ipg payment and old registration
 *
 * @author g.shahrokhabadi
 */
@Data
public class BillPaymentReqDto implements Serializable {
    private static final long serialVersionUID = 9150338863684768818L;

    @Schema(title = " شماره قبض")
    @NotBlank
    private String invoiceNumber;

    @Schema(title = " شماره پرداخت")
    @NotBlank
    private String paymentNumber;

    @Schema(title = " مبلغ قبض")
    @NotBlank
    private String amount;

}
