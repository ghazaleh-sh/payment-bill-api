package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.InvoicePaymantReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.entities.Invoice;

public interface InvoicePaymentService {

    Invoice verifyInvoicePayment(String token, String orderId);

//    String getToken(InvoiceRegistration invoiceRegistration);

    GeneralRegistrationResponse invoiceRegister(InvoicePaymantReqDto invoicePaymantReqDto);

    InvoiceVerifyReqDto BillPaymentByIpg(InvoicePaymantReqDto invoicePaymantReqDto, UserVO userVo, String authToken);

    FinalBillPaymentResDto finalBillPaymentByIpg(InvoiceVerifyReqDto invoiceVerifyReqDto);
}
