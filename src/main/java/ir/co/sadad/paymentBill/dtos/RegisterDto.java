package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public abstract class RegisterDto implements Serializable {
    @JsonProperty("MerchantId")
    protected String merchantId;
    @JsonProperty("TerminalId")
    protected String terminalId;
    @JsonProperty("Amount")
    protected String amount;
    @JsonProperty("ReturnUrl")
    protected String returnUrl;
    @JsonProperty("AdditionalData")
    protected String additionalData;
    @JsonProperty("OrderId")
    protected Long orderId;

    public RegisterDto(String merchantId, String terminalId, String amount, String returnUrl, String additionalData, Long orderId) {
        this.merchantId = merchantId;
        this.terminalId = terminalId;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.additionalData = additionalData;
        this.orderId = orderId;
    }

}