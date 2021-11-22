package ir.co.sadad.paymentBill.controllers;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.InvoiceRequestDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.services.InvoicePaymentService;
import ir.co.sadad.paymentBill.validations.InvoiceValid;
import ir.co.sadad.paymentBill.validations.VerifyValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.ws.rs.core.Response;

import static ir.co.sadad.paymentBill.Constants.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "invoice")
public class InvoiceController {

    private final InvoicePaymentService invoicePaymentService;

//    @GetMapping(value = "/id")
//    public Response find(@RequestParam("id") Integer id) {
//        return Response.ok().build();
//    }

    @PostMapping(value = "/register")
    public ResponseEntity<InvoiceVerifyDto> create(@InvoiceValid @RequestBody InvoiceRequestDto invoiceRequestDto) {

        InvoiceVerifyDto verifyResponseDto = invoicePaymentService.invoiceRegister(invoiceRequestDto);
        return new ResponseEntity<>(verifyResponseDto, HttpStatus.OK);
    }

//    @PutMapping
//    public Response verifyInvoicePayment(@VerifyValid @RequestBody InvoiceVerifyDto invoiceVerifyDto) {
//        return Response.status(200).entity(invoicePaymentService.verifyInvoicePayment(invoiceVerifyDto.getToken(), invoiceVerifyDto.getOrderId())).build();
//    }

    @PostMapping(value = "/ipg-bill-payment")
    public ResponseEntity<InvoiceVerifyDto> billPaymentByIpg(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
            @RequestHeader(USER_ID) String userId,
            @RequestHeader(SERIAL_ID) String serialId,
            @RequestHeader(CELL_PHONE) String cellPhone,
            @RequestHeader(SSN) String ssn,
            @InvoiceValid @RequestBody InvoiceRequestDto invoiceRequestDto) {

//        String userId = "158";
//        String cellPhone ="09218301631";
//         String serialId = "5700cd58-3cd6-4ce3-81ff-ee519e1f6df7";
//         String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJncmFudCI6IkNMSUVOVCIsImlzcyI6Imh0dHA6Ly9hcGkuYm1pLmlyL3NlY3VyaXR5IiwiYXVkIjoia2V5IiwiZXhwIjoxNjM3MTI5NjQxNzQxLCJuYmYiOjE2MzcwNDMyNDE3NDEsInJvbGUiOiIiLCJzZXJpYWwiOiIzYTY2YTFkMC00NTE1LTNhMjMtOTFmMS03NzYzMzA4NmI1OGUiLCJzc24iOiIxMjMiLCJjbGllbnRfaWQiOiIxMjMiLCJzY29wZXMiOlsiY3VzdG9tZXItc3VwZXIiXX0=.fNPgh_2w-r5mYzwhKN2tTyf_YOYQ6YV3GkEail6S3ck";
//         String ssn = "0079993141";

        InvoiceVerifyDto billPaymentResDto = invoicePaymentService.BillPaymentByIpg(invoiceRequestDto, UserVO.of(userId, cellPhone, serialId, ssn), authToken);

        return new ResponseEntity<>(billPaymentResDto, HttpStatus.OK);

    }

    @PostMapping(value = "/ipg-bill-verify")
    public ResponseEntity<FinalBillPaymentResDto> finalBillPaymentByIpg(
            @VerifyValid @RequestBody InvoiceVerifyDto invoiceVerifyDto) {

        FinalBillPaymentResDto finalIpgVerifyRes = invoicePaymentService.finalBillPaymentByIpg(invoiceVerifyDto);

        return new ResponseEntity<>(finalIpgVerifyRes, HttpStatus.OK);

    }
}
