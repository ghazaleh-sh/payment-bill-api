package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.BillInquiryReqDto;
import ir.co.sadad.paymentBill.dtos.BillInquiryResDto;
import ir.co.sadad.paymentBill.dtos.BillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.BillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.entities.Invoice;

public interface InvoicePaymentService {

    Invoice verifyInvoicePayment(String token, String orderId);

    BillPaymentResDto invoiceRegister(BillPaymentReqDto billPaymentReqDto);

    BillPaymentResDto BillPaymentByIpg(BillPaymentReqDto billPaymentReqDto, UserVO userVo, String authToken);

    FinalBillPaymentResDto finalBillPaymentByIpg(FinalBillPaymentReqDto finalBillPaymentReqDto);

    BillInquiryResDto billInquiry(BillInquiryReqDto billInquiryReqDto);
}
