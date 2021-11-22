package ir.co.sadad.paymentBill.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoiceVerifyDto {

    private String token;
    private String orderId;

}
