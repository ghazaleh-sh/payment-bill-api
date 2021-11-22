package ir.co.sadad.paymentBill.dtos.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Deprecated
@Setter
@Getter
public class PspPspInvoiceRegistrationRegistrationReqDtoRequset extends PspInvoiceRegistrationReqDto implements Serializable {
    @JsonProperty("SignData")
    private String signData;

    public PspPspInvoiceRegistrationRegistrationReqDtoRequset(String merchantId, String terminalId, String amount, String returnUrl, String additionalData, Long orderId, String billId, String payId, String signData) {
        super(merchantId, terminalId, amount, returnUrl, additionalData, orderId, billId, payId, signData);
        this.signData = signData;
    }

    public static class Builder {

        private PspInvoiceRegistrationReqDto pspInvoiceRegistrationReqDto;
        private String signData;

        public Builder addAlreadyExistRegisterInvoice(PspInvoiceRegistrationReqDto pspInvoiceRegistrationReqDto) {
            this.pspInvoiceRegistrationReqDto = pspInvoiceRegistrationReqDto;
            return this;
        }

        public Builder signData(String signData) {
            this.signData = signData;
            return this;
        }

        public PspPspInvoiceRegistrationRegistrationReqDtoRequset build() {
            return new PspPspInvoiceRegistrationRegistrationReqDtoRequset(this.pspInvoiceRegistrationReqDto.getMerchantId(), this.pspInvoiceRegistrationReqDto.getTerminalId(), this.pspInvoiceRegistrationReqDto.getAmount(), this.pspInvoiceRegistrationReqDto.getReturnUrl()
                    , this.pspInvoiceRegistrationReqDto.getAdditionalData(), this.pspInvoiceRegistrationReqDto.getOrderId(), this.pspInvoiceRegistrationReqDto.getInvoiceNumber(), this.pspInvoiceRegistrationReqDto.getPaymentNumber(), this.signData);
        }
    }

}
