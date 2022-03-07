package ir.co.sadad.paymentBill.dtos;

import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * request of bill inquiry service
 *
 * @author g.shahrokhabadi
 */
@Getter
public class BillInquiryReqDto implements Serializable {

    private static final long serialVersionUID = -1873174039863373316L;
    @NotBlank
    private String invoiceNumber;
    @NotBlank
    private String paymentNumber;

    public void setInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber.isEmpty())
            throw new BillPaymentException("method.argument.not.valid", HttpStatus.BAD_REQUEST);

        String tempInvoiceId = "0000000000000" + invoiceNumber;
        String invoiceIdSubstring = tempInvoiceId.substring(tempInvoiceId.length() - 13);
        this.invoiceNumber = invoiceIdSubstring;
    }

    public void setPaymentNumber(String paymentNumber) {
        if (paymentNumber.isEmpty())
            throw new BillPaymentException("method.argument.not.valid", HttpStatus.BAD_REQUEST);

        String tempPaymentId = "0000000000000" + paymentNumber;
        String paymentIdSubstring = tempPaymentId.substring(tempPaymentId.length() - 13);
        this.paymentNumber = paymentIdSubstring;
    }

}
