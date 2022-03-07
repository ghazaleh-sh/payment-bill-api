package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.PaymentBillApiApplicationTests;
import ir.co.sadad.paymentBill.commons.UserVO;
import ir.co.sadad.paymentBill.dtos.*;
import ir.co.sadad.paymentBill.enums.IpgVerificationStatus;
import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.services.basics.BasicWebClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

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

    private BillPaymentReqDto billPaymentReqDto;
    private FinalBillPaymentReqDto finalBillPaymentReqDto;
    private BillInquiryReqDto billInquiryReqDto;


/////////////////////////// billPaymentByIpg method /////////////////////////////

    @Test
    void shouldThrowExceptionForPaidRecordByBillPaymentByIpg(){
        billPaymentReqDto = new BillPaymentReqDto();
        billPaymentReqDto.setAmount("262000");
        billPaymentReqDto.setInvoiceNumber("4337680730155");
        billPaymentReqDto.setPaymentNumber("0000026206938");

        assertThrows(BillPaymentException.class, () -> service.BillPaymentByIpg(billPaymentReqDto, userVO, authToken));

    }

    @Test
    void shouldReturnTrueByBillPaymentByIpg() {
        billPaymentReqDto = new BillPaymentReqDto();
        billPaymentReqDto.setAmount("262000");
        billPaymentReqDto.setInvoiceNumber("4337680730155");
        billPaymentReqDto.setPaymentNumber("26206938");

        IPGPaymentRequestResDto pspRes = new IPGPaymentRequestResDto();
        pspRes.setMerchantId("aaa");
        pspRes.setToken("123");
        pspRes.setRequestId("23233323");
        pspRes.setTerminalId("123");
        when(ipgRequestWebClient.doCallService(any(), any(), any(), any())).thenReturn(pspRes);

        BillPaymentResDto billPaymentResDto = service.BillPaymentByIpg(billPaymentReqDto, userVO, authToken);

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
        finalBillPaymentReqDto.setRequestId("1637649541535");
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

    /////////////////////////// bill inquiry method /////////////////////////////
    @Test
    void shouldThrowNotValidExceptionByInquiry() {
        billInquiryReqDto = new BillInquiryReqDto();

        assertThrows(BillPaymentException.class, () -> billInquiryReqDto.setInvoiceNumber(""));
    }

    @Test
    void shouldThrowNotExistExceptionByInquiry() {
        billInquiryReqDto = new BillInquiryReqDto();
        billInquiryReqDto.setInvoiceNumber("4337680730100");
        billInquiryReqDto.setPaymentNumber("100571");

        assertThrows(BillPaymentException.class, () -> service.billInquiry(billInquiryReqDto));
    }

    @Test
    void shouldReturnTrueByInquiry() {
        billInquiryReqDto = new BillInquiryReqDto();
        billInquiryReqDto.setInvoiceNumber("3768073015510");
        billInquiryReqDto.setPaymentNumber("26206938");

        BillInquiryResDto billInquiryResDto = service.billInquiry(billInquiryReqDto);
        assertEquals("3768073015510", billInquiryResDto.getInvoiceNumber());
    }
}