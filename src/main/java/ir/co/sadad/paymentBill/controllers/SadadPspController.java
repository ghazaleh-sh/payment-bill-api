package ir.co.sadad.paymentBill.controllers;

import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.dtos.GeneralVerificationResponse;
import ir.co.sadad.paymentBill.dtos.PspInvoiceRegistrationReqDto;
import ir.co.sadad.paymentBill.dtos.payment.*;
import ir.co.sadad.paymentBill.services.AdviceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Deprecated
@Slf4j
@RequiredArgsConstructor
@RestController
public class SadadPspController {

    private final AdviceApi advice;

    @PostMapping(value = "/TokenizedBillPaymentApi/BillVerify")
    GeneralVerificationResponse verifyInvoice(@RequestParam("Token") String token, @RequestParam("SignData") String signData, @RequestParam("orderId") String orderId) {
        return null;
    }

    @PostMapping(value = "/Advice/Verify")
    GeneralVerificationResponse verifyPayment(@RequestParam("Token") String token, @RequestParam("SignData") String signData, @RequestParam("orderId") String orderId) {
        return null;
    }

    @PostMapping(value = "/TokenizedBillPaymentApi/BillRequest")
    GeneralRegistrationResponse registerInvoice(PspInvoiceRegistrationReqDto pspInvoiceRegistrationReqDto) {
        return null;
    }

    @PostMapping(value = "/Request/PaymentRequest")
    GeneralRegistrationResponse registerPayment(PspPaymentRegistrationRegistrationRequest pspPaymentRegistrationRequest) {
        return null;
    }
}
