package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * dto for response of verification ipg service
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPGVerifyResDto implements Serializable {

    private static final long serialVersionUID = -6282621006413961854L;
    @JsonProperty("SwitchResCode")
    private String switchResCode;
    @JsonProperty("HashedCardNo")
    private String hashedCardNo;
    @JsonProperty("CardNo")
    private String cardNo;
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
