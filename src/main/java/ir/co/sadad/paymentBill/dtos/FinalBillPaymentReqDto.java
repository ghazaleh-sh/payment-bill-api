package ir.co.sadad.paymentBill.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * this dto will come from body request of ipg service
 *
 * @author g.shahrokhabadi
 */

@Data
public class FinalBillPaymentReqDto implements Serializable {

    private static final long serialVersionUID = -3908148881558742932L;
    /**
     * extracted from oauth token when calling ipg-payment-request service and saved in transaction table
     */
    @Schema(title = "شناسه کاربر")
    private String userId;

    @Schema(title = "شماره سفارش قبض")
    private String requestId;

    @Schema(title = "توکن پرداخت")
    private String token;

    @Schema(title = "شماره سریال موبایل")
    private String userDeviceId;

    private String primaryAccNo;

}
