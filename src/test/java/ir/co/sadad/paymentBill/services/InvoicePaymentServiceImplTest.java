package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.PaymentBillApiApplicationTests;
import ir.co.sadad.paymentBill.dtos.InvoicePaymentReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.*;
import ir.co.sadad.paymentBill.enums.IpgVerificationStatus;
import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.services.basics.BasicWebClient;
import ir.co.sadad.paymentBill.validations.InvoiceValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
class InvoicePaymentServiceImplTest extends PaymentBillApiApplicationTests {

    @Autowired
    private InvoicePaymentService service;

    @MockBean
    BasicWebClient<IPGPaymentRequestReqDto, IPGPaymentRequestResDto> ipgRequestWebClient;

    @MockBean
    BasicWebClient<IPGVerifyReqDto, IPGVerifyResDto> ipgVerifyWebClient;

    private InvoicePaymentReqDto invoicePaymentReqDto;
    private FinalBillPaymentReqDto finalBillPaymentReqDto;

//    @Autowired
    private InvoiceValidator validator = new InvoiceValidator();

/////////////////////////// billPaymentByIpg method /////////////////////////////
    @Test
    void shouldReturnValidationErrorByBillPaymentByIpg(){
        invoicePaymentReqDto = new InvoicePaymentReqDto();
        invoicePaymentReqDto.setAmount("262000");
        invoicePaymentReqDto.setInvoiceNumber("433768073015511");
        invoicePaymentReqDto.setPaymentNumber("26206938");

//        boolean violations = validator.isValid(invoicePaymentReqDto , new ConstraintValidatorContextImpl(null,null,null,null,null,null));

//        assertThrows(BillPaymentException.class, validator.checkInvoiceValidation(invoicePaymentReqDto.getInvoiceNumber() , invoicePaymentReqDto.getPaymentNumber(), new BigDecimal(invoicePaymentReqDto.getAmount())));

    }

    @Test
    void shouldThrowExceptionForPaidRecordByBillPaymentByIpg(){
        invoicePaymentReqDto = new InvoicePaymentReqDto();
        invoicePaymentReqDto.setAmount("262000");
        invoicePaymentReqDto.setInvoiceNumber("4337680730155");
        invoicePaymentReqDto.setPaymentNumber("0000026206938");

        assertThrows(CodedException.class, () -> service.BillPaymentByIpg(invoicePaymentReqDto, userVO, authToken));

    }

    @Test
    void shouldReturnTrueByBillPaymentByIpg() {
        invoicePaymentReqDto = new InvoicePaymentReqDto();
        invoicePaymentReqDto.setAmount("262000");
        invoicePaymentReqDto.setInvoiceNumber("4337680730155");
        invoicePaymentReqDto.setPaymentNumber("26206938");

        IPGPaymentRequestResDto pspRes = new IPGPaymentRequestResDto();
        pspRes.setMerchantId("aaa");
        pspRes.setToken("123");
        pspRes.setRequestId("23233323");
        pspRes.setTerminalId("123");
        when(ipgRequestWebClient.doCallService(any(), any(), any(), any())).thenReturn(pspRes);

        InvoiceVerifyReqDto billPaymentResDto = service.BillPaymentByIpg(invoicePaymentReqDto, userVO, authToken);

        assertEquals(pspRes.getToken(), billPaymentResDto.getToken());

    }


    /////////////////////////// finalBillPaymentByIpg method /////////////////////////////
    @Test
    void shouldThrowUserIdExceptionByFinalBillPaymentByIpg() {
        finalBillPaymentReqDto = new FinalBillPaymentReqDto();
        finalBillPaymentReqDto.setRequestId("1637991304096");
        finalBillPaymentReqDto.setToken("20");
        finalBillPaymentReqDto.setUserId("80"); // incorrect
        finalBillPaymentReqDto.setUserDeviceId(userVO.getSerialId());

        assertThrows(BillPaymentException.class, () -> service.finalBillPaymentByIpg(finalBillPaymentReqDto));

    }

    @Test
    void shouldThrowOrderIdExceptionByFinalBillPaymentByIpg() {
        finalBillPaymentReqDto = new FinalBillPaymentReqDto();
        finalBillPaymentReqDto.setRequestId("12"); // doesn't exist
        finalBillPaymentReqDto.setToken("20");
        finalBillPaymentReqDto.setUserId(userVO.getUserId());
        finalBillPaymentReqDto.setUserDeviceId(userVO.getSerialId());

        assertThrows(BillPaymentException.class, () -> service.finalBillPaymentByIpg(finalBillPaymentReqDto));

    }

    @Test
    void shouldReturnTrueByFinalBillPaymentByIpg() {
        finalBillPaymentReqDto = new FinalBillPaymentReqDto();
        finalBillPaymentReqDto.setRequestId("1637991304096");
        finalBillPaymentReqDto.setToken("20");
        finalBillPaymentReqDto.setUserId(userVO.getUserId());
        finalBillPaymentReqDto.setUserDeviceId(userVO.getSerialId());

        IPGVerifyResDto pspVerifyRes = new IPGVerifyResDto();
        pspVerifyRes.setAmount(262000L);
        pspVerifyRes.setResCode(0);
        pspVerifyRes.setOrderId(1637991304096L);
        when(ipgVerifyWebClient.doCallService((IPGVerifyReqDto) any(), any(), any())).thenReturn(pspVerifyRes);

        FinalBillPaymentResDto FinalBillPaymentResDto = service.finalBillPaymentByIpg(finalBillPaymentReqDto);

        assertEquals(FinalBillPaymentResDto.getStatus(), IpgVerificationStatus.SUCCESSFUL);

    }


}