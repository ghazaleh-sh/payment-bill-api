package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * dto for response of psp after registration
 *
 * @author g.shahrokhabadi
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralRegistrationResponse {

    @JsonProperty("ResCode")
    private String resCode;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("SwitchResCode")
    private String switchResCode;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("BillType")
    private String billType;
    @JsonProperty("BillOrganName")
    private String billOrganName;

}
