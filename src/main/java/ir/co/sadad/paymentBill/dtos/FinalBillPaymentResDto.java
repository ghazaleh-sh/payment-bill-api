package ir.co.sadad.paymentBill.dtos;


import ir.co.sadad.paymentBill.enums.IpgVerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * response of verification and finalized  payment by ipg
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class FinalBillPaymentResDto implements Serializable {

    private static final long serialVersionUID = -4371989718508151233L;
    private IpgVerificationStatus status;
}
