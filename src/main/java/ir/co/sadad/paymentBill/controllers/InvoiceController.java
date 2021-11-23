package ir.co.sadad.paymentBill.controllers;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.InvoicePaymantReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.services.InvoicePaymentService;
import ir.co.sadad.paymentBill.validations.InvoiceValid;
import ir.co.sadad.paymentBill.validations.VerifyValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ir.co.sadad.paymentBill.Constants.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "invoice")
public class InvoiceController {

    private final InvoicePaymentService invoicePaymentService;

    @GetMapping(value = "/id")
    public ResponseEntity<HttpStatus> find(@RequestParam("id") Integer id) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping//(value = "/register")
    public ResponseEntity<GeneralRegistrationResponse> create(@InvoiceValid @RequestBody InvoicePaymantReqDto invoicePaymantReqDto) {

        GeneralRegistrationResponse paymentResponseDto = invoicePaymentService.invoiceRegister(invoicePaymantReqDto);
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> verifyInvoicePayment(@VerifyValid @RequestBody InvoiceVerifyReqDto invoiceVerifyReqDto) {
        invoicePaymentService.verifyInvoicePayment(invoiceVerifyReqDto.getToken(), invoiceVerifyReqDto.getOrderId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/ipg-bill-payment")
    public ResponseEntity<InvoiceVerifyReqDto> billPaymentByIpg(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
            @RequestHeader(USER_ID) String userId,
            @RequestHeader(SERIAL_ID) String serialId,
            @RequestHeader(CELL_PHONE) String cellPhone,
            @RequestHeader(SSN) String ssn,
            @InvoiceValid @RequestBody InvoicePaymantReqDto invoicePaymantReqDto) {

        InvoiceVerifyReqDto billPaymentResDto = invoicePaymentService.BillPaymentByIpg(invoicePaymantReqDto, UserVO.of(userId, cellPhone, serialId, ssn), authToken);

        return new ResponseEntity<>(billPaymentResDto, HttpStatus.OK);

    }

    @PostMapping(value = "/ipg-bill-verify")
    public ResponseEntity<FinalBillPaymentResDto> finalBillPaymentByIpg(
            @VerifyValid @RequestBody InvoiceVerifyReqDto invoiceVerifyReqDto) {

        FinalBillPaymentResDto finalIpgVerifyRes = invoicePaymentService.finalBillPaymentByIpg(invoiceVerifyReqDto);

        return new ResponseEntity<>(finalIpgVerifyRes, HttpStatus.OK);

    }
}