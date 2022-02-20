package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * dto for response of verification ipg service
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPGVerifyResDto {

    @JsonProperty("SwitchResCode")
    protected String switchResCode;
    @JsonProperty("HashedCardNo")
    protected String hashedCardNo;
    @JsonProperty("CardNo")
    protected String cardNo;
    @JsonProperty("ResCode")
    private Integer resCode;
    @JsonProperty("Amount")
    private Long amount;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("RetrivalRefNo")
    private String retrievalRefNo;
    @JsonProperty("SystemTraceNo")
    private String systemTraceNo;
    @JsonProperty("OrderId")
    private Long orderId;
    @JsonProperty("TransactionDate")
    private String transactionDate;
    @JsonProperty("AdditionalData")
    private String additionalData;
}
