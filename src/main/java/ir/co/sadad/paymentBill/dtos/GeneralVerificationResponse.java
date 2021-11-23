package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

//import javax.json.bind.annotation.JsonbProperty;

@Setter
@Getter
public class GeneralVerificationResponse {

    @JsonProperty("ResCode")
    protected String resCode;
    @JsonProperty("Amount")
    protected String amount;
    @JsonProperty("Description")
    protected String description;
    @JsonProperty("RetrivalRefNo")
    protected String retrivalRefNo;
    @JsonProperty("SystemTraceNo")
    protected String systemTraceNo;
    @JsonProperty("OrderId")
    protected String orderId;
    @JsonProperty("SwitchResCode")
    protected String switchResCode;
    @JsonProperty("HashedCardNo")
    protected String hashedCardNo;
    @JsonProperty("CardNo")
    protected String cardNo;

}
