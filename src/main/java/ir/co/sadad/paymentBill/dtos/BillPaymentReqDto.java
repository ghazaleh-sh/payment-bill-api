package ir.co.sadad.paymentBill.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * dto for first request of payment- using by ipg payment and old registration
 *
 * @author g.shahrokhabadi
 */
@Data
public class BillPaymentReqDto {
    @Schema(title = " شماره قبض")
    @NotBlank
    private String invoiceNumber;

    @Schema(title = " شماره پرداخت")
    @NotBlank
    private String paymentNumber;

    @Schema(title = " مبلغ قبض")
    @NotBlank
    private String amount;

//
//    public boolean isEmpty() {
//        if (this.invoiceNumber == null && this.paymentNumber == null && this.amount == null)
//            return true;
//        else return false;
//    }

}
