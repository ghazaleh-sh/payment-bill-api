package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import ir.co.sadad.paymentBill.enums.*;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * response of bill inquiry service
 *
 * @author g.shahrokhabadi
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillInquiryResDto {

    private Long orderId;

    private String invoiceNumber;

    private String paymentNumber;

    private BigDecimal amount;

    private String userId;

    private String deviceSerialId;

    /**
     * such as bill of water, electric, gas ,etc.
     */
    private InvoiceType invoiceType;

    /**
     * PAYMENT_TYPE: by card
     */
    private ServiceMethod serviceMethod;

    /**
     * status of payment
     */
    @NotNull
    private PaymentStatus paymentStatus;

    /**
     * HAM_BAAM channel
     */
    private Channel channel;

    /**
     * melli or mellat - is null
     */
    private ServiceProvider serviceProvider;

    /**
     * card number used for the paid bills - is null for unpaid and inconclusive status
     * this field name will be changed to "sourceNo" by invoice-integration router
     */
    private String cardNo;

    private Long creationDateTime;

    private Long updateDateTime;

    private String realTransactionDateTime;

    private String traceNumber;

    private String referenceNumber;

    private String hashedCardNo;

    private String transactionDescription;

}
