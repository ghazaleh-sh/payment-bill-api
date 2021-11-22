package ir.co.sadad.paymentBill.entities;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class InvoicePk implements Serializable {

    @Size(max = 13)
    private String invoiceNumber;
    @Size(max = 13)
    private String paymentNumber;

    public InvoicePk() {
    }

    public InvoicePk(String invoiceNumber, String paymentNumber) {
        this.invoiceNumber = invoiceNumber;
        this.paymentNumber = paymentNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }
}
