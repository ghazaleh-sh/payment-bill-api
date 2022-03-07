package ir.co.sadad.paymentBill.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.bmi.identity.security.interceptor.Scope;
import ir.co.sadad.paymentBill.commons.UserVO;
import ir.co.sadad.paymentBill.dtos.BillInquiryReqDto;
import ir.co.sadad.paymentBill.dtos.BillInquiryResDto;
import ir.co.sadad.paymentBill.dtos.BillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.BillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentResDto;
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

import static ir.co.sadad.paymentBill.commons.Constants.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "invoice")
@Tag(description = "کنترلر های سرویس پرداخت قبض", name = "Invoice controller")
public class InvoiceController {

    private final InvoicePaymentService invoicePaymentService;

//    @GetMapping(value = "/id")
//    public ResponseEntity<HttpStatus> find(@RequestParam("id") Integer id) {
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @PostMapping(value = "/oldRegister")
    @Operation(summary = "سرویس دریافت مستقیم توکن از پی اس پی", description = "این سرویس برای تست ارتباط مستقیم با پی اس پی نگه داشته شده است. ")
    public ResponseEntity<BillPaymentResDto> create(@InvoiceValid @Valid @RequestBody BillPaymentReqDto billPaymentReqDto) {

        BillPaymentResDto paymentResponseDto = invoicePaymentService.invoiceRegister(billPaymentReqDto);
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "سرویس تایید پرداخت از پی اس پی بطور مستقیم", description = "این سرویس برای تست ارتباط مستقیم با پی اس پی نگه داشته شده است. ")
    public ResponseEntity<HttpStatus> verifyInvoicePayment(@VerifyValid @RequestBody BillPaymentResDto invoiceVerifyReqDto) {
        invoicePaymentService.verifyInvoicePayment(invoiceVerifyReqDto.getToken(), invoiceVerifyReqDto.getOrderId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "سرویس درخواست توکن از پی اس پی", description = "سرویسی که با ارسال مشخصات قبض، توکن پی اس پی را از طریق ipg دریافت میکند")
    @PostMapping(value = "/ipg-bill-payment")
    public ResponseEntity<BillPaymentResDto> billPaymentByIpg(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
            @RequestHeader(USER_ID) String userId,
            @RequestHeader(SERIAL_ID) String serialId,
            @RequestHeader(CELL_PHONE) String cellPhone,
            @RequestHeader(SSN) String ssn,
            @InvoiceValid @Valid @RequestBody BillPaymentReqDto billPaymentReqDto) {

        BillPaymentResDto billPaymentResDto = invoicePaymentService.BillPaymentByIpg(billPaymentReqDto, UserVO.of(userId, cellPhone, serialId, ssn), authToken);

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
    public ResponseEntity<BillInquiryResDto> inquiry(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                                     @Valid @RequestBody BillInquiryReqDto billInquiryReqDto) {
        return new ResponseEntity<>(invoicePaymentService.billInquiry(billInquiryReqDto), HttpStatus.OK);
    }
}