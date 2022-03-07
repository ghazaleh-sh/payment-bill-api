package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * dto for response of psp after verification
 *
 * @author g.shahrokhabadi
 */
@Setter
@Getter
public class GeneralVerificationResponse implements Serializable {

    private static final long serialVersionUID = 5552640496595186952L;
    @JsonProperty("ResCode")
    private String resCode;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("RetrivalRefNo")
    private String retrivalRefNo;
    @JsonProperty("SystemTraceNo")
    private String systemTraceNo;
    @JsonProperty("OrderId")
    private String orderId;
    @JsonProperty("SwitchResCode")
    private String switchResCode;
    @JsonProperty("HashedCardNo")
    private String hashedCardNo;
    @JsonProperty("CardNo")
    private String cardNo;

}
