package ir.co.sadad.paymentBill.entities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class InvoicePk implements Serializable {

    private static final long serialVersionUID = -2006387710941550321L;
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
}
