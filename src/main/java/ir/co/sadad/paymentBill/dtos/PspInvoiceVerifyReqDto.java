package ir.co.sadad.paymentBill.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * dto for sending request to psp service- old version
 *
 * @author g.shahrokhabadi
 */
@Setter
@Getter
public class PspInvoiceVerifyReqDto {

    @JsonProperty("Token")
    private String token;
    @JsonProperty("SignData")
    private String signData;

}
