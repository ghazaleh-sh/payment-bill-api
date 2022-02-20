package ir.co.sadad.paymentBill.dtos;


import ir.co.sadad.paymentBill.enums.IpgVerificationStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * response of verification and finalized  payment by ipg
 *
 * @author g.shahrokhabadi
 */

@Getter
@Setter
public class FinalBillPaymentResDto {

    private IpgVerificationStatus status;
}
