package ir.co.sadad.paymentBill.dtos;

import javax.validation.constraints.NotBlank;

/**
 * dto for first request of payment- using by ipg and old registration
 *
 * @author g.shahrokhabadi
 */
public class InvoicePaymentReqDto {

    @NotBlank
    private String invoiceNumber;
    @NotBlank
    private String paymentNumber;
    @NotBlank
    private String amount;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        String tempInvoiceId = "0000000000000" + invoiceNumber;
        String invoiceIdSubstring = tempInvoiceId.substring(tempInvoiceId.length() - 13);
        this.invoiceNumber = invoiceIdSubstring;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        String tempPaymentId = "0000000000000" + paymentNumber;
        String paymentIdSubstring = tempPaymentId.substring(tempPaymentId.length() - 13);
        this.paymentNumber = paymentIdSubstring;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
