package ir.co.sadad.paymentBill.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.bmi.identity.security.interceptor.Scope;
import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.BillInquiryReqDto;
import ir.co.sadad.paymentBill.dtos.BillInquiryResDto;
import ir.co.sadad.paymentBill.dtos.InvoicePaymentReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.services.InvoicePaymentService;
import ir.co.sadad.paymentBill.validations.InvoiceValid;
import ir.co.sadad.paymentBill.validations.VerifyValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static ir.co.sadad.paymentBill.Constants.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "invoice")
@Tag(description = "کنترلر های سرویس پرداخت قبض", name = "Invoice controller")
public class InvoiceController {

    private final InvoicePaymentService invoicePaymentService;

    @GetMapping(value = "/id")
    public ResponseEntity<HttpStatus> find(@RequestParam("id") Integer id) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/oldRegister")
    public ResponseEntity<InvoiceVerifyReqDto> create(@InvoiceValid @RequestBody InvoicePaymentReqDto invoicePaymentReqDto) {

        InvoiceVerifyReqDto paymentResponseDto = invoicePaymentService.invoiceRegister(invoicePaymentReqDto);
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> verifyInvoicePayment(@VerifyValid @RequestBody InvoiceVerifyReqDto invoiceVerifyReqDto) {
        invoicePaymentService.verifyInvoicePayment(invoiceVerifyReqDto.getToken(), invoiceVerifyReqDto.getOrderId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "سرویس درخواست توکن از پی اس پی", description = "سرویسی که با ارسال مشخصات قبض، توکن پی اس پی را از طریق ipg دریافت میکند")
    @PostMapping(value = "/ipg-bill-payment")
    public ResponseEntity<InvoiceVerifyReqDto> billPaymentByIpg(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
            @RequestHeader(USER_ID) String userId,
            @RequestHeader(SERIAL_ID) String serialId,
            @RequestHeader(CELL_PHONE) String cellPhone,
            @RequestHeader(SSN) String ssn,
            @InvoiceValid @RequestBody InvoicePaymentReqDto invoicePaymentReqDto) {

        InvoiceVerifyReqDto billPaymentResDto = invoicePaymentService.BillPaymentByIpg(invoicePaymentReqDto, UserVO.of(userId, cellPhone, serialId, ssn), authToken);

        return new ResponseEntity<>(billPaymentResDto, HttpStatus.OK);

    }

    @Scope(values = "ipg-payment-verify")
    @Operation(summary = "سرویس تایید پرداخت پی اس پی", description = "سرویسی که جهت تایید پرداخت توسط پی اس پی از طریق درگاه ipg کال میشود.")
    @PostMapping(value = "/ipg-bill-verify")
    public ResponseEntity<FinalBillPaymentResDto> finalBillPaymentByIpg(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestBody FinalBillPaymentReqDto finalBillPaymentReqDto) {

        FinalBillPaymentResDto finalIpgVerifyRes = invoicePaymentService.finalBillPaymentByIpg(finalBillPaymentReqDto);

        return new ResponseEntity<>(finalIpgVerifyRes, HttpStatus.OK);

    }

    @Operation(summary = "سرویس استعلام قبض", description = "سرویسی که با دریافت شناسه قبض و شناسه پرداخت موارد آنرا استعلام میکند")
    @PostMapping(value = "/bill-inquiry")
    public ResponseEntity<BillInquiryResDto> inquiry(@Valid @RequestBody BillInquiryReqDto billInquiryReqDto) {
        return new ResponseEntity<>(invoicePaymentService.billInquiry(billInquiryReqDto), HttpStatus.OK);
    }
}