package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.BillInquiryReqDto;
import ir.co.sadad.paymentBill.dtos.BillInquiryResDto;
import ir.co.sadad.paymentBill.dtos.InvoicePaymentReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.entities.Invoice;

public interface InvoicePaymentService {

    Invoice verifyInvoicePayment(String token, String orderId);

    InvoiceVerifyReqDto invoiceRegister(InvoicePaymentReqDto invoicePaymentReqDto);

    InvoiceVerifyReqDto BillPaymentByIpg(InvoicePaymentReqDto invoicePaymentReqDto, UserVO userVo, String authToken);

    FinalBillPaymentResDto finalBillPaymentByIpg(FinalBillPaymentReqDto finalBillPaymentReqDto);

    BillInquiryResDto billInquiry(BillInquiryReqDto billInquiryReqDto);
}
