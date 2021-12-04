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

//    public static class Builder {
//        private String merchantId, terminalId, amount, returnUrl, additionalData, invoiceNumber, paymentNumber, signData;
//        private Long orderId;
//
//        public Builder merchantId(String merchantId) {
//            this.merchantId = merchantId;
//            return this;
//        }
//
//        public Builder terminalId(String terminalId) {
//            this.terminalId = terminalId;
//            return this;
//        }
//
//        public Builder amount(String amount) {
//            this.amount = amount;
//            return this;
//        }
//
//        public Builder returnUrl(String returnUrl) {
//            this.returnUrl = returnUrl;
//            return this;
//        }
//
//        public Builder additionalData(String additionalData) {
//            this.additionalData = additionalData;
//            return this;
//        }
//
//        public Builder invoiceNumber(String invoiceNumber) {
//            this.invoiceNumber = invoiceNumber;
//            return this;
//        }
//
//        public Builder paymentNumber(String paymentNumber) {
//            this.paymentNumber = paymentNumber;
//            return this;
//        }
//
//        public Builder orderId(Long orderId) {
//            this.orderId = orderId;
//            return this;
//        }
//
//        public Builder signData(String signData) {
//            this.signData = signData;
//            return this;
//        }
//
////        public Register buildRegister() {
////            return new Register(this.merchantId, this.terminalId, this.amount, this.returnUrl, this.additionalData, this.orderId);
////        }
//
//        public PspInvoiceRegistrationReqDto build() {
//            return new PspInvoiceRegistrationReqDto(this.merchantId, this.terminalId, this.amount, this.returnUrl, this.additionalData, this.orderId, this.invoiceNumber, this.paymentNumber, this.signData);
//        }
//
//    }
}