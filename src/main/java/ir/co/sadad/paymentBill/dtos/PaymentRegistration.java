package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRegistration extends RegisterDto {

    @JsonProperty("MultiplexingData")
    String multiplexingData;
    @JsonProperty("UserId")
    Long userId;
    @JsonProperty("ApplicationName")
    String applicationName;

    public PaymentRegistration(String merchantId, String terminalId, String amount, String returnUrl, String additionalData, Long orderId, String multiplexingData, Long userId, String applicationName) {
        super(merchantId, terminalId, amount, returnUrl, additionalData, orderId);
        this.multiplexingData = multiplexingData;
        this.userId = userId;
        this.applicationName = applicationName;
    }

    public static class Builder {
        private String merchantId, terminalId, amount, returnUrl, additionalData, multiplexingData, applicationName;
        private Long orderId, userId;

        public Builder merchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder terminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public Builder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public Builder returnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
            return this;
        }

        public Builder additionalData(String additionalData) {
            this.additionalData = additionalData;
            return this;
        }

        public Builder multiplexingData(String multiplexingData) {
            this.multiplexingData = multiplexingData;
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

//        public Register buildRegister() {
//            return new Register(this.merchantId, this.terminalId, this.amount, this.returnUrl, this.additionalData, this.orderId);
//        }

        public PaymentRegistration buildRegisterInvoice() {
            return new PaymentRegistration(this.merchantId, this.terminalId, this.amount, this.returnUrl, this.additionalData, this.orderId, this.multiplexingData, this.userId, this.applicationName);
        }

    }

}