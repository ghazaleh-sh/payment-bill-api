package ir.co.sadad.paymentBill.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "PAY_REQUEST_INVOICE")
public class PayRequestInvoice {

    public PayRequestInvoice() {
    }

    public PayRequestInvoice(PayRequest payRequest, Invoice invoice) {
        this.creationDateTime = new Timestamp(DateTime.now().getMillis());
        this.invoice = invoice;
        this.payRequest = payRequest;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PAY_REQUEST_ID", referencedColumnName = "ID", nullable = false)
    private PayRequest payRequest;

    @Column(name = "CREATION_DATE_TIME", nullable = false)
    private Timestamp creationDateTime;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "INVOICE_NUMBER", referencedColumnName = "INVOICE_NUMBER", nullable = false)
            , @JoinColumn(name = "PAYMENT_NUMBER", referencedColumnName = "PAYMENT_NUMBER", nullable = false)})

    @JsonIgnore
    private Invoice invoice;

    @Override
    public String toString() {
        return "PayRequestInvoice{" +
                "payRequestInvoiceId='" + id + '\'' +
                ", creationDateTime=" + creationDateTime +
                '}';
    }

}
