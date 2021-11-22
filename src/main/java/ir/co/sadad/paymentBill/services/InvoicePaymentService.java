package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.InvoiceRequestDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.entities.Invoice;

public interface InvoicePaymentService {

    Invoice verifyInvoicePayment(String token, String orderId);

//    String getToken(InvoiceRegistration invoiceRegistration);

    InvoiceVerifyDto invoiceRegister(InvoiceRequestDto invoiceRequestDto);

    InvoiceVerifyDto BillPaymentByIpg(InvoiceRequestDto invoiceRequestDto, UserVO userVo, String authToken);

    FinalBillPaymentResDto finalBillPaymentByIpg(InvoiceVerifyDto invoiceVerifyDto);
}
