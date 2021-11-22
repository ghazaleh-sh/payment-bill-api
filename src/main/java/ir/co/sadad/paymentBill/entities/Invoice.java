package ir.co.sadad.paymentBill.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.co.sadad.paymentBill.enums.*;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "INVOICE")
//@NamedQueries({
//        @NamedQuery(name = Invoice.NAMED_QUERY_FIND_BY_INVOCICENUMBER_PAYMENTNUMBER,
//                query = "SELECT invc FROM Invoice invc WHERE invc.invoiceNumber = :invoiceNumber and invc.paymentNumber = :paymentNumber"),
//        @NamedQuery(name = Invoice.NAMED_QUERY_FIND_BY_ID,
//                query = "SELECT invc FROM Invoice invc WHERE invc.id = :id"),
//        @NamedQuery(name = Invoice.NAMED_QUERY_FIND_BY_ORDER_ID,
//                query = "SELECT invc FROM Invoice invc WHERE invc.orderId = :orderId")
//})
@IdClass(InvoicePk.class)
public class Invoice {
//    public static final String NAMED_QUERY_FIND_BY_INVOCICENUMBER_PAYMENTNUMBER = "InvoiceService.findByInvoiceNumberAndPaymentNumber";
//    public static final String NAMED_QUERY_FIND_BY_ID = "InvoiceService.findById";
//    public static final String NAMED_QUERY_FIND_BY_ORDER_ID = "InvoiceService.findByOrderId";

    public Invoice() {
    }


//    public Invoice(String invoiceNumber, String paymentNumber, InvoiceType invoiceType, BigDecimal amount, ServiceMethod serviceMethod, PaymentStatus paymentStatus, Channel channel, String userId, String deviceSerialId) {
//        this.invoiceType = invoiceType;
//        this.amount = amount;
//        this.paymentStatus = paymentStatus;
//        this.serviceMethod = serviceMethod;
//        this.channel = channel;
//        this.creationDateTime = new Timestamp(DateTime.now().getMillis());
//        this.orderId = DateTime.now().getMillis();
//        this.invoiceNumber = invoiceNumber;
//        this.paymentNumber = paymentNumber;
//        this.userId = userId;
//        this.deviceSerialId = deviceSerialId;
//    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID", nullable = false)
    @NotNull
    private Long orderId;

    @Id
    @Column(name = "INVOICE_NUMBER", length = 13, nullable = false)
    @Size(max = 13)
    @NotNull
    private String invoiceNumber;

    @Id
    @Column(name = "PAYMENT_NUMBER", length = 13, nullable = false)
    @Size(max = 13)
    @NotNull
    private String paymentNumber;

    @Column(name = "INVOICE_TYPE", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private InvoiceType invoiceType;

    @Column(name = "AMOUNT", nullable = false)
    @NotNull
    private BigDecimal amount;

    @Column(name = "CREATION_DATE_TIME", nullable = false)
    @NotNull
    private Timestamp creationDateTime;

    @Column(name = "PAYMENT_TYPE", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ServiceMethod serviceMethod;

    @Column(name = "PAYMENT_STATUS", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus paymentStatus;

    @Column(name = "CHANNEL", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Channel channel;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "invoice")
    @JsonBackReference
    private Collection<PayRequestInvoice> payRequestInvoices;

    @Column(name = "REAL_TRANSACTION_DATETIME")
    private Timestamp realTransactionDateTime;

    @Column(name = "TRACE_NUMBER", length = 10)
    private String traceNumber;

    @Column(name = "REFERENCE_NUMBER", length = 15)
    private String referenceNumber;

    @Column(name = "UPDATE_DATE_TIME")
    private Timestamp updateDateTime;

    @Column(name = "SERVICE_PROVIDER")
    @Enumerated(EnumType.STRING)
    private ServiceProvider serviceProvider;

    @Column(name = "HASHED_CARD_NO")
    private String hashedCardNo;

    @Column(name = "CARD_NO")
    private String cardNo;

    @Column(name = "TRANSACTION_DESCRIPTION")
    private String transactionDescription;

    @Column(name = "USER_ID", length = 15)
    private String userId;

    @Column(name = "DEVICE_SERIAL_ID", length = 40)
    private String deviceSerialId;

}
