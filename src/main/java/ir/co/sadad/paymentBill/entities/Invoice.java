package ir.co.sadad.paymentBill.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.co.sadad.paymentBill.enums.*;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "INVOICE")
@IdClass(InvoicePk.class)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 369718894750463303L;

    public Invoice() {
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID", nullable = false)
    @NotNull
    private Long orderId;

    @Id
    @Column(name = "INVOICE_NUMBER", length = 13, nullable = false, columnDefinition = "CHAR(13) NOT NULL")
    @Size(max = 13)
    @NotNull
    private String invoiceNumber;

    @Id
    @Column(name = "PAYMENT_NUMBER", length = 13, nullable = false, columnDefinition = "CHAR(13) NOT NULL")
    @Size(max = 13)
    @NotNull
    private String paymentNumber;

    @Column(name = "INVOICE_TYPE", length = 10, nullable = false, columnDefinition = "CHAR NOT NULL")
    @Enumerated(EnumType.STRING)
    @NotNull
    private InvoiceType invoiceType;

    @Column(name = "AMOUNT", nullable = false)
    @NotNull
    private BigDecimal amount;

    @Column(name = "CREATION_DATE_TIME", nullable = false)
    @NotNull
    private Timestamp creationDateTime;

    @Column(name = "PAYMENT_TYPE", length = 15, nullable = false, columnDefinition = "CHAR(15) NOT NULL")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ServiceMethod serviceMethod;

    @Column(name = "PAYMENT_STATUS", length = 15, nullable = false, columnDefinition = "CHAR NOT NULL")
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus paymentStatus;

    @Column(name = "CHANNEL", length = 15, nullable = false, columnDefinition = "CHAR(15) NOT NULL")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Channel channel;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "invoice")
    @JsonBackReference
    private Collection<PayRequestInvoice> payRequestInvoices;

    @Column(name = "REAL_TRANSACTION_DATETIME")
    private Timestamp realTransactionDateTime;

    @Column(name = "TRACE_NUMBER", length = 10, columnDefinition = "CHAR(10)")
    private String traceNumber;

    @Column(name = "REFERENCE_NUMBER", length = 15, columnDefinition = "CHAR")
    private String referenceNumber;

    @Column(name = "UPDATE_DATE_TIME")
    private Timestamp updateDateTime;

    @Column(name = "SERVICE_PROVIDER")
    @Enumerated(EnumType.STRING)
    private ServiceProvider serviceProvider;

    @Column(name = "HASHED_CARD_NO")
    private String hashedCardNo;

    @Column(name = "CARD_NO", columnDefinition = "CHAR")
    private String cardNo;

    @Column(name = "TRANSACTION_DESCRIPTION")
    private String transactionDescription;

    @Column(name = "USER_ID", length = 15, columnDefinition = "CHAR(15)")
    private String userId;

    @Column(name = "DEVICE_SERIAL_ID", length = 40)
    private String deviceSerialId;

}
