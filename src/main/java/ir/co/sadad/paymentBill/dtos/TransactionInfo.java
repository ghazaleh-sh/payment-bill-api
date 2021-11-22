package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TransactionInfo {

    private String status;
    private String orderId;
    private String referenceCode;
    private String traceNo;
    private LocalDateTime transactionDateTime;

    public TransactionInfo(String status, String orderId, String referenceCode, String traceNo, LocalDateTime tranactionDateTime) {
        this.status = status;
        this.orderId = orderId;
        this.referenceCode = referenceCode;
        this.traceNo = traceNo;
        this.transactionDateTime = tranactionDateTime;
    }

    public static class Builder {

        private String status;
        private String orderId;
        private String referenceCode;
        private String traceNo;
        private LocalDateTime transactionDateTime;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder referenceCode(String referenceId) {
            this.referenceCode = referenceId;
            return this;
        }

        public Builder traceNo(String traceNo) {
            this.traceNo = traceNo;
            return this;
        }
        public Builder transactionDateTime(LocalDateTime transactionDateTime){
            this.transactionDateTime = transactionDateTime;
            return this;
        }
        public TransactionInfo build(){
            return new TransactionInfo(this.status, this.orderId, this.referenceCode, this.traceNo, this.transactionDateTime);
        }
    }
}
