package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
//@SuperBuilder(toBuilder = true)
public class PspInvoiceRegistrationReqDto extends RegisterDto implements Serializable {
    @JsonProperty("BillId")
    protected String invoiceNumber;
    @JsonProperty("PayId")
    protected String paymentNumber;
    @JsonProperty("SignData")
    private String signData;

    @Builder
    public PspInvoiceRegistrationReqDto(String merchantId, String terminalId, String amount, String returnUrl, String additionalData, Long orderId, String invoiceNumber, String paymentNumber, String signData) {
        super(merchantId, terminalId, amount, returnUrl, additionalData, orderId);
        this.invoiceNumber = invoiceNumber;
        this.paymentNumber = paymentNumber;
        this.signData = signData;
    }

}