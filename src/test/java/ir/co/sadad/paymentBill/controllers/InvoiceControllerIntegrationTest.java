package ir.co.sadad.paymentBill.controllers;

import ir.co.sadad.paymentBill.dtos.BillInquiryReqDto;
import ir.co.sadad.paymentBill.dtos.InvoicePaymentReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.enums.IpgVerificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static ir.co.sadad.paymentBill.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceControllerIntegrationTest extends AbstractBaseIntegrationTest{

    @Test
    void shouldPassBillPaymentByIpg() {

        InvoicePaymentReqDto paymentReqDto = new InvoicePaymentReqDto();
        paymentReqDto.setAmount("1000");
        paymentReqDto.setPaymentNumber("106936");
        paymentReqDto.setInvoiceNumber("4337680730155");

        InvoiceVerifyReqDto result = webTestClient
                .post()
                .uri("/invoice/ipg-bill-payment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + clientToken.getAccess_token())
                .header(USER_ID, userId)
                .header(SERIAL_ID, deviceId)
                .header(CELL_PHONE, cellphone)
                .header(SSN, ssn)
                .body(Mono.just(paymentReqDto), InvoicePaymentReqDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(InvoiceVerifyReqDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals("tokenbill+", result.getToken());
    }

    @Test
    void shouldPassFinalBillPaymentByIpg() {

        FinalBillPaymentReqDto finalBillPaymentReqDto = new FinalBillPaymentReqDto();
        finalBillPaymentReqDto.setToken("tokenbill+");
        finalBillPaymentReqDto.setRequestId("1638087133235");
        finalBillPaymentReqDto.setUserId("158");

        FinalBillPaymentResDto result = webTestClient
                .post()
                .uri("/invoice/ipg-bill-verify")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + clientToken.getAccess_token())
                .body(Mono.just(finalBillPaymentReqDto), FinalBillPaymentReqDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(FinalBillPaymentResDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(IpgVerificationStatus.SUCCESSFUL, result.getStatus());

    }

    @Test
    void shouldPassInquiry() {

        BillInquiryReqDto billInquiryReq = new BillInquiryReqDto();
        billInquiryReq.setPaymentNumber("106936");
        billInquiryReq.setInvoiceNumber("4337680730155");

         webTestClient
                .post()
                .uri("/invoice/bill-inquiry")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(billInquiryReq), BillInquiryReqDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}