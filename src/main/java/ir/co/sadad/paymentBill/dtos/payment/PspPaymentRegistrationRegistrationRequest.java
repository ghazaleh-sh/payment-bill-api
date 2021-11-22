package ir.co.sadad.paymentBill.dtos.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PspPaymentRegistrationRegistrationRequest extends PaymentRegistration {
    @JsonProperty("SignData")
    private String signData;

    public PspPaymentRegistrationRegistrationRequest(String merchantId, String terminalId, String amount, String returnUrl, String additionalData, Long orderId, String multiplexingData, Long userId, String applicationName, String signData) {
        super(merchantId, terminalId, amount, returnUrl, additionalData, orderId, multiplexingData, userId, applicationName);
        this.signData = signData;
    }

    public static class Builder {

        private PaymentRegistration paymentRegistration;
        private String signData;

        public Builder addAlreadyExistRegisteredPayment(PaymentRegistration paymentRegistration) {
            this.paymentRegistration = paymentRegistration;
            return this;
        }

        public Builder signData(String signData) {
            this.signData = signData;
            return this;
        }

        public PspPaymentRegistrationRegistrationRequest build() {
            return new PspPaymentRegistrationRegistrationRequest(this.paymentRegistration.getMerchantId(), this.paymentRegistration.getTerminalId(), this.paymentRegistration.getAmount(), this.paymentRegistration.getReturnUrl()
                    , this.paymentRegistration.getAdditionalData(), this.paymentRegistration.getOrderId(), this.paymentRegistration.getMultiplexingData(), this.paymentRegistration.getUserId(), this.paymentRegistration.getApplicationName(), this.signData);
        }
    }


}