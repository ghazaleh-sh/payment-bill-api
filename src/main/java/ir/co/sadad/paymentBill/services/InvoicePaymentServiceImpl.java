package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.*;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentReqDto;
import ir.co.sadad.paymentBill.dtos.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.IPGPaymentRequestReqDto;
import ir.co.sadad.paymentBill.dtos.IPGVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.PaymentVerificationResDto;
import ir.co.sadad.paymentBill.entities.Invoice;
import ir.co.sadad.paymentBill.entities.PayRequest;
import ir.co.sadad.paymentBill.entities.PayRequestInvoice;
import ir.co.sadad.paymentBill.entities.Payee;
import ir.co.sadad.paymentBill.enums.*;
import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.repositories.InvoiceRepository;
import ir.co.sadad.paymentBill.repositories.PayRequestInvoiceRepository;
import ir.co.sadad.paymentBill.repositories.PayRequestRepository;
import ir.co.sadad.paymentBill.repositories.PayeeRepository;
import ir.co.sadad.paymentBill.commons.Encoder;
import ir.co.sadad.paymentBill.commons.ResourceBundleFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static ir.co.sadad.paymentBill.Constants.PAYMENT_BILL_SERVICE_TYPE;

/**
 * a service to handle payments by psp directly, via ipg and inquiry the bill info.
 *
 * @author g.shahrokhabadi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InvoicePaymentServiceImpl implements InvoicePaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PayeeRepository payeeRepository;
    private final PayRequestRepository payRequestRepository;
    private final PayRequestInvoiceRepository payRequestInvoiceRepository;
    private final Encoder encoder;
    private final SadadPspService sadadPspService;

    @Value(value = "${invoice.terminalId}")
    String terminalId;

    @Value(value = "${invoice.merchantId}")
    String merchantId;

    @Value(value = "${invoice.returnUrl}")
    String returnUrl;

    @Value(value = "${old-registration.userId}")
    private String userId;
    @Value(value = "${old-registration.cellPhone}")
    private String cellPhone;
    @Value(value = "${old-registration.serialId}")
    private String serialId;


    /**
     * old method to get token of psp and registers invoice details in tables(if not exist)
     *
     * @param billPaymentReqDto
     * @return
     */
    @SneakyThrows
    @Override
    public BillPaymentResDto invoiceRegister(BillPaymentReqDto billPaymentReqDto) {

        Invoice savedInvoice = invoiceCreation(billPaymentReqDto, UserVO.of(userId, cellPhone, serialId));

        PspInvoiceRegistrationReqDto pspInvoiceRegistrationReqDto = prepareInvoiceRegistration(savedInvoice);

        String signData = encoder.prepareSignDataForRegistration(pspInvoiceRegistrationReqDto.getTerminalId(), String.valueOf(pspInvoiceRegistrationReqDto.getOrderId()), pspInvoiceRegistrationReqDto.getAmount());
        pspInvoiceRegistrationReqDto.setSignData(signData);
        GeneralRegistrationResponse generalRegistrationResponse = sadadPspService.registerInvoiceByPsp(pspInvoiceRegistrationReqDto);

        String token = processGetTokenResponse(generalRegistrationResponse);

        BillPaymentResDto verifyResponse = new BillPaymentResDto();
        verifyResponse.setOrderId(String.valueOf(savedInvoice.getOrderId()));
        verifyResponse.setToken(token);
        return verifyResponse;
    }

    /**
     * old method to verify the result of request payment
     *
     * @param token
     * @param orderId
     * @return
     */
    @Override
    public Invoice verifyInvoicePayment(String token, String orderId) {

        if (token == null || token.isEmpty()) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E400005", "EINP40010009");
        }
        String base64SignedData = encoder.prepareSignDataWithToken(token);
        PaymentVerificationResDto paymentVerificationResDto = sadadPspService.verifyInvoiceByPsp(token, base64SignedData, orderId);
        return updateTransactionInfo(paymentVerificationResDto);
    }

    /**
     * takes token of psp through ipg services
     *
     * @param billPaymentReqDto
     * @param userVo
     * @param authToken
     * @return pspToken and orderId
     */
    @SneakyThrows
    @Override
    public BillPaymentResDto BillPaymentByIpg(BillPaymentReqDto billPaymentReqDto, UserVO userVo, String authToken) {

        Invoice savedInvoice = invoiceCreation(billPaymentReqDto, userVo);

        String pspToken = sadadPspService.requestPaymentByIpg(makeIpgPaymentRequest(savedInvoice, billPaymentReqDto, userVo), authToken);

        BillPaymentResDto billPaymentResDto = new BillPaymentResDto();
        billPaymentResDto.setToken(pspToken);
        billPaymentResDto.setOrderId(savedInvoice.getOrderId().toString());
        return billPaymentResDto;

    }

    /**
     * verifies the result of payment bill by ipg services
     *
     * @param finalBillPaymentReqDto
     * @return
     */
    @Override
    public FinalBillPaymentResDto finalBillPaymentByIpg(FinalBillPaymentReqDto finalBillPaymentReqDto) {

        Optional<Invoice> checkResult = invoiceRepository.findByOrderId(Long.valueOf(finalBillPaymentReqDto.getRequestId()));
        Invoice singleResult = checkResult.orElseThrow(() -> new BillPaymentException("bill.is.not.exist.by.order.id", HttpStatus.BAD_REQUEST));

        if (singleResult.getPaymentStatus().equals(PaymentStatus.PAID))
            throw new BillPaymentException("bill.is.paid", HttpStatus.BAD_REQUEST);

        if (!finalBillPaymentReqDto.getUserId().equals(singleResult.getUserId()))
            throw new BillPaymentException("userId.is.not.the.same", HttpStatus.BAD_REQUEST);

        PaymentVerificationResDto paymentVerifyRes = sadadPspService.verifyBillPaymentByIpg(makeIpgVerifyRequest(finalBillPaymentReqDto));

        updateTransactionInfo(paymentVerifyRes);

        FinalBillPaymentResDto finalResponse = new FinalBillPaymentResDto();
        finalResponse.setStatus(IpgVerificationStatus.SUCCESSFUL);
        return finalResponse;
    }

    /**
     * inquiries the bill information
     *
     * @param billInquiryReqDto
     * @return
     */
    public BillInquiryResDto billInquiry(BillInquiryReqDto billInquiryReqDto) {
        Invoice invoice = invoiceRepository.findByInvoiceNumberAndPaymentNumber(billInquiryReqDto.getInvoiceNumber(), billInquiryReqDto.getPaymentNumber()).orElseThrow(
                () -> new BillPaymentException("invoice.is.not.exist", HttpStatus.NOT_FOUND));

        return BillInquiryResDto.builder()
                .amount(invoice.getAmount())
                .cardNo(invoice.getCardNo())
                .channel(invoice.getChannel())
                .deviceSerialId(invoice.getDeviceSerialId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .invoiceType(invoice.getInvoiceType())
                .orderId(invoice.getOrderId())
                .paymentNumber(billInquiryReqDto.getPaymentNumber())
                .paymentStatus(invoice.getPaymentStatus())
                .serviceMethod(invoice.getServiceMethod())
                .serviceProvider(invoice.getServiceProvider())
                .userId(invoice.getUserId())
                .traceNumber(invoice.getTraceNumber())
                .updateDateTime(invoice.getUpdateDateTime() == null ? null : invoice.getUpdateDateTime().getTime())
                .referenceNumber(invoice.getReferenceNumber())
                .build();

    }

    private IPGPaymentRequestReqDto makeIpgPaymentRequest(Invoice savedinvoice, BillPaymentReqDto billPaymentReqDto, UserVO user) {
        IPGPaymentRequestReqDto req = new IPGPaymentRequestReqDto();
        req.setAmount(Long.valueOf(billPaymentReqDto.getAmount()));
        req.setServiceType(PAYMENT_BILL_SERVICE_TYPE);
        req.setRequestId(savedinvoice.getOrderId().toString());
        req.setUserDeviceId(user.getSerialId());
        req.setUserId(user.getUserId());
        req.setSsn(user.getSsn());
        req.setInvoiceNumber(billPaymentReqDto.getInvoiceNumber());
        req.setPaymentNumber(billPaymentReqDto.getPaymentNumber());
        return req;
    }

    private IPGVerifyReqDto makeIpgVerifyRequest(FinalBillPaymentReqDto req) {
        IPGVerifyReqDto ipgVerifyReq = new IPGVerifyReqDto();
        ipgVerifyReq.setRequestId(req.getRequestId());
        ipgVerifyReq.setToken(req.getToken());
        ipgVerifyReq.setUserId(req.getUserId());
        ipgVerifyReq.setServiceType(PAYMENT_BILL_SERVICE_TYPE);
        return ipgVerifyReq;
    }

    private Invoice invoiceCreation(BillPaymentReqDto billPaymentReqDto, UserVO userVo) {
        Optional<Invoice> invoice = invoiceRepository.findByInvoiceNumberAndPaymentNumber(billPaymentReqDto.getInvoiceNumber(), billPaymentReqDto.getPaymentNumber());
        Invoice savedInvoice = invoice.orElseGet(() -> makeNewInvoice(billPaymentReqDto, userVo));

        if (savedInvoice.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new BillPaymentException("bill.is.paid", HttpStatus.BAD_REQUEST);
        }

        Optional<Payee> payee = payeeRepository.findByPayeeIdentifier(userVo.getUserMobileNo());
        Payee savedPayee = payee.orElseGet(() -> new Payee(userVo.getUserMobileNo()));
        payeeRepository.saveAndFlush(savedPayee);

        Optional<PayRequest> payRequest = payRequestRepository.findByPayeeAndChannel(savedPayee, Channel.BAM_PAY);
        PayRequest savedPayReq = payRequest.orElseGet(() -> new PayRequest(savedPayee, Channel.BAM_PAY, null));
        payRequestRepository.saveAndFlush(savedPayReq);

        Optional<PayRequestInvoice> payReqInvoice = payRequestInvoiceRepository.findByPayRequestIdAndInvoiceId(savedPayReq.getId(), savedInvoice.getId());
        PayRequestInvoice savedPayReqInvoice = payReqInvoice.orElseGet(() -> new PayRequestInvoice(savedPayReq, savedInvoice));
        payRequestInvoiceRepository.saveAndFlush(savedPayReqInvoice);

        log.info("tables of payment-bill created or updated successfully....");
        return savedInvoice;
    }

    private Invoice makeNewInvoice(BillPaymentReqDto billPaymentReqDto, UserVO userVo) {
        InvoiceType invoiceType = InvoiceType.getEnum(Integer.parseInt(billPaymentReqDto.getInvoiceNumber().substring(billPaymentReqDto.getInvoiceNumber().length() - 2, billPaymentReqDto.getInvoiceNumber().length() - 1)));

        Invoice savedInvoice = new Invoice();
        savedInvoice.setInvoiceNumber(billPaymentReqDto.getInvoiceNumber());
        savedInvoice.setPaymentNumber(billPaymentReqDto.getPaymentNumber());
        savedInvoice.setInvoiceType(invoiceType);
        savedInvoice.setAmount(new BigDecimal(billPaymentReqDto.getAmount()));
        savedInvoice.setServiceMethod(ServiceMethod.BY_CARD);
        savedInvoice.setPaymentStatus(PaymentStatus.INCONCLUSIVE);
        savedInvoice.setChannel(Channel.HAM_BAAM);
        savedInvoice.setDeviceSerialId(userVo.getSerialId());
        savedInvoice.setUserId(userVo.getUserId());
        savedInvoice.setOrderId(DateTime.now().getMillis());
        savedInvoice.setCreationDateTime(new Timestamp(DateTime.now().getMillis()));
        invoiceRepository.saveAndFlush(savedInvoice);

        return savedInvoice;
    }

    private PspInvoiceRegistrationReqDto prepareInvoiceRegistration(Invoice invoice) {
        return PspInvoiceRegistrationReqDto.builder()
                .terminalId(this.terminalId)
                .merchantId(this.merchantId)
                .amount(String.valueOf(invoice.getAmount().intValue()))
                .invoiceNumber(invoice.getInvoiceNumber())
                .paymentNumber(invoice.getPaymentNumber())
                .returnUrl(this.returnUrl)
                .orderId(invoice.getOrderId())
                .build();
    }

    private Invoice updateTransactionInfo(PaymentVerificationResDto transactionInfoRes) {
        Optional<Invoice> checkInvoice = invoiceRepository.findByOrderId(transactionInfoRes.getOrderId());
        Invoice existingInvoice = checkInvoice.orElseThrow(() -> new BillPaymentException("bill.is.not.exist.by.order.id", HttpStatus.BAD_REQUEST));

        String status = transactionInfoRes.getStatusCode().equals(0) ? PaymentStatus.PAID.toString() : PaymentStatus.UNPAID.toString();

        existingInvoice.setPaymentStatus(PaymentStatus.getEnum(status));
        existingInvoice.setUpdateDateTime(new Timestamp(DateTime.now().getMillis()));
        existingInvoice.setTraceNumber(transactionInfoRes.getTraceNo());
        existingInvoice.setReferenceNumber(transactionInfoRes.getReferenceNo());
        existingInvoice.setRealTransactionDateTime(Timestamp.valueOf(transactionInfoRes.getTransactionDate()));
        existingInvoice.setCardNo(transactionInfoRes.getCardNo());
        existingInvoice.setHashedCardNo(transactionInfoRes.getHashedCardNo());
        existingInvoice.setTransactionDescription(transactionInfoRes.getDescription());

        log.info("invoice table updated transaction successfully after verifying ....");
        return invoiceRepository.saveAndFlush(existingInvoice);
    }

    public static String processGetTokenResponse(GeneralRegistrationResponse generalRegistrationResponse) throws BillPaymentException {
        String resCode = generalRegistrationResponse.getResCode();
        if (resCode.equals("0")) {
            return generalRegistrationResponse.getToken();
        } else {
            ResourceBundle resCodeResourceBundle = new ResourceBundleFactory().produceBundleWithLocale(Locale.ENGLISH, "ResponseCode");
            ResourceBundle switchResponseCodeBundle = new ResourceBundleFactory().produceBundleWithLocale(Locale.ENGLISH, "SwitchResponseCode");

            String switchResCode = generalRegistrationResponse.getSwitchResCode();
            String responseMessage = resCode != null ? resCodeResourceBundle.getString(resCode) : null;
            String switchResponseMessage = (switchResCode == null || switchResCode.equals("")) ? null : switchResponseCodeBundle.getString(switchResCode);

            StringBuffer messages = new StringBuffer();
            messages.append("ResMsg:").append(responseMessage).append(". ").append("SwitchResMsg").append(switchResponseMessage);
            log.error(messages.toString());
            throw new BillPaymentException(responseMessage, Integer.decode(resCode), HttpStatus.BAD_REQUEST);
        }
    }
}
