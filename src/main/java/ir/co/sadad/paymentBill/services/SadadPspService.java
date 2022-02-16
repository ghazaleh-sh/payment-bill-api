package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.dtos.PspInvoiceRegistrationReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.PaymentVerificationResDto;

public interface SadadPspService {
    String requestPaymentByIpg(IPGPaymentRequestReqDto ipgPaymentRequestReqDto, String oauthToken);

    PaymentVerificationResDto verifyBillPaymentByIpg(IPGVerifyReqDto ipgVerifyReqDto);

    GeneralRegistrationResponse registerInvoiceByPsp(PspInvoiceRegistrationReqDto pspInvoiceRegistrationReq);

    PaymentVerificationResDto verifyInvoiceByPsp(String token, String signData, String orderId);

}
