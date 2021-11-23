package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.InvoicePaymantReqDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.dtos.GeneralVerificationResponse;
import ir.co.sadad.paymentBill.dtos.PspInvoiceRegistrationReqDto;
import ir.co.sadad.paymentBill.dtos.payment.PaymentVerificationResponse;
import ir.co.sadad.paymentBill.entities.Invoice;
import ir.co.sadad.paymentBill.entities.PayRequest;
import ir.co.sadad.paymentBill.entities.PayRequestInvoice;
import ir.co.sadad.paymentBill.entities.Payee;
import ir.co.sadad.paymentBill.enums.*;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.exceptions.TokenGenerationException;
import ir.co.sadad.paymentBill.repositories.InvoiceRepository;
import ir.co.sadad.paymentBill.repositories.PayRequestInvoiceRepository;
import ir.co.sadad.paymentBill.repositories.PayRequestRepository;
import ir.co.sadad.paymentBill.repositories.PayeeRepository;
import ir.co.sadad.paymentBill.common.Encoder;
import ir.co.sadad.paymentBill.common.ResourceBundleFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static ir.co.sadad.paymentBill.Constants.PAYMENT_BILL_SERVICE_TYPE;

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

//    TokenDecoder tokenDecoder;

    @Override
    public Invoice verifyInvoicePayment(String token, String orderId) {

        if (token == null || token.isEmpty()) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E400005", "EINP40010009");
        }
        String base64SignedData = encoder.prepareSignDataWithToken(token);
        GeneralVerificationResponse generalVerificationResponse = sadadPspService.verifyInvoiceByPsp(token, base64SignedData, orderId);
        return processVerifyResponse(processVerifyResponse(generalVerificationResponse));
    }

    @SneakyThrows
    @Override
    public GeneralRegistrationResponse invoiceRegister(InvoicePaymantReqDto invoicePaymantReqDto) {

        String userId = "158";
        String cellPhone ="09218301631";
        String serialId = "5700cd58-3cd6-4ce3-81ff-ee519e1f6df7";

        Invoice savedinvoice = invoiceCreation(invoicePaymantReqDto, UserVO.of(userId, cellPhone, serialId));

        PspInvoiceRegistrationReqDto pspInvoiceRegistrationReqDto = prepareInvoiceRegistration(savedinvoice);

        String signData = encoder.prepareSignDataForRegistration(pspInvoiceRegistrationReqDto.getTerminalId(), String.valueOf(pspInvoiceRegistrationReqDto.getOrderId()), pspInvoiceRegistrationReqDto.getAmount());
        pspInvoiceRegistrationReqDto.setSignData(signData);
        GeneralRegistrationResponse generalRegistrationResponse = sadadPspService.registerInvoiceByPsp(pspInvoiceRegistrationReqDto);
        return generalRegistrationResponse;

//        String token = processGetTokenResponse(generalRegistrationResponse);

//        InvoiceVerifyReqDto verifyResponse = new InvoiceVerifyReqDto();
//        verifyResponse.setOrderId(String.valueOf(savedinvoice.getOrderId()));
//        verifyResponse.setToken(token);
//        return verifyResponse;
    }

    @Override
    public InvoiceVerifyReqDto BillPaymentByIpg(InvoicePaymantReqDto invoicePaymantReqDto, UserVO userVo, String authToken){

        Invoice savedinvoice = invoiceCreation(invoicePaymantReqDto, userVo);

        String pspToken = sadadPspService.requestPaymentByIpg(makeIpgPaymentRequest(savedinvoice, invoicePaymantReqDto,userVo) , authToken);

        InvoiceVerifyReqDto billPaymentResDto = new InvoiceVerifyReqDto();
        billPaymentResDto.setToken(pspToken);
        billPaymentResDto.setOrderId(savedinvoice.getOrderId().toString());
        return billPaymentResDto;

    }

    @Override
    public FinalBillPaymentResDto finalBillPaymentByIpg(InvoiceVerifyReqDto invoiceVerifyReqDto){
        //Validated before
        Optional<Invoice> singleResult = invoiceRepository.findByOrderId(Long.valueOf(invoiceVerifyReqDto.getOrderId()));

        GeneralVerificationResponse generalVerificationResponse = sadadPspService.verifyBillPaymentByIpg(makeIpgVerifyRequest(invoiceVerifyReqDto,singleResult.get().getUserId()));

        processVerifyResponse(processVerifyResponse(generalVerificationResponse));

        FinalBillPaymentResDto  finalResponse = new FinalBillPaymentResDto();
        finalResponse.setStatus(IpgVerificationStatus.SUCCESSFUL);
        return finalResponse;
    }

    private IPGPaymentRequestReqDto makeIpgPaymentRequest(Invoice savedinvoice, InvoicePaymantReqDto invoicePaymantReqDto, UserVO user){
        IPGPaymentRequestReqDto req = new IPGPaymentRequestReqDto();
        req.setAmount(Long.valueOf(invoicePaymantReqDto.getAmount()));
        req.setServiceType(PAYMENT_BILL_SERVICE_TYPE);
        req.setRequestId(savedinvoice.getOrderId().toString());
        req.setUserDeviceId(user.getSerialId());
        req.setUserId(user.getUserId());
        req.setSsn(user.getSsn());
        req.setInvoiceNumber(invoicePaymantReqDto.getInvoiceNumber());
        req.setPaymentNumber(invoicePaymantReqDto.getPaymentNumber());
        return req;
    }

    private IPGVerifyReqDto makeIpgVerifyRequest(InvoiceVerifyReqDto req, String userId) {
        IPGVerifyReqDto ipgVerifyReq = new IPGVerifyReqDto();
        ipgVerifyReq.setRequestId(req.getOrderId());
        ipgVerifyReq.setToken(req.getToken());
        ipgVerifyReq.setUserId(userId);
        ipgVerifyReq.setServiceType(PAYMENT_BILL_SERVICE_TYPE);
        return ipgVerifyReq;
    }

    private Invoice invoiceCreation(InvoicePaymantReqDto invoicePaymantReqDto, UserVO userVo){
        Optional<Invoice> invoice = invoiceRepository.findByInvoiceNumberAndPaymentNumber(invoicePaymantReqDto.getInvoiceNumber(), invoicePaymantReqDto.getPaymentNumber());
        Invoice savedinvoice = invoice.orElse(makeNewInvoice(invoicePaymantReqDto, userVo));

        if (savedinvoice.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new CodedException(ExceptionType.DuplicateResourceCodedException, "E4090001", "EINP40010001");
        }

        Optional<Payee> payee = payeeRepository.findByPayeeIdentifier(userVo.getUserMobileNo());
        Payee savedPayee = payee.orElse(new Payee(userVo.getUserMobileNo()));
        payeeRepository.saveAndFlush(savedPayee);

        Optional<PayRequest> payRequest = payRequestRepository.findByPayeeAndChannel(savedPayee, Channel.HAM_BAAM);
        PayRequest savedPayReq = payRequest.orElse(new PayRequest(savedPayee, Channel.HAM_BAAM, null));
        payRequestRepository.saveAndFlush(savedPayReq);

        Optional<PayRequestInvoice> payReqInvoice = payRequestInvoiceRepository.findByPayRequestIdAndInvoiceId(savedPayReq.getId(), savedinvoice.getId());
        PayRequestInvoice savedPayReqInvoice = payReqInvoice.orElse(new PayRequestInvoice(savedPayReq, savedinvoice));
        payRequestInvoiceRepository.saveAndFlush(savedPayReqInvoice);

        return savedinvoice;
    }

    private Invoice makeNewInvoice(InvoicePaymantReqDto invoicePaymantReqDto, UserVO userVo) {
        InvoiceType invoiceType = InvoiceType.getEnum(Integer.parseInt(invoicePaymantReqDto.getInvoiceNumber().substring(invoicePaymantReqDto.getInvoiceNumber().length() - 2, invoicePaymantReqDto.getInvoiceNumber().length() - 1)));

        Invoice savedInvoice = new Invoice();
        savedInvoice.setInvoiceNumber(invoicePaymantReqDto.getInvoiceNumber());
        savedInvoice.setPaymentNumber(invoicePaymantReqDto.getPaymentNumber());
        savedInvoice.setInvoiceType(invoiceType);
        savedInvoice.setAmount(new BigDecimal(invoicePaymantReqDto.getAmount()));
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

        return new PspInvoiceRegistrationReqDto.Builder().terminalId(this.terminalId)
                .merchantId(this.merchantId)
                .amount(String.valueOf(invoice.getAmount().intValue()))
                .invoiceNumber(invoice.getInvoiceNumber())
                .paymentNumber(invoice.getPaymentNumber())
                .returnUrl(this.returnUrl)
                .orderId(invoice.getOrderId())
                .build();
    }

    /**
     *  process
     *
     * @param response
     * @return
     */

    public Invoice processVerifyResponse(PaymentVerificationResponse response) {
        if (response == null) {
            throw new CodedException(ExceptionType.VerifyFailedCodedException, "EINP40010002", "EINP40010002");
        } else {
            String state;
            String errorMessage;
            Integer responseCode = Integer.valueOf(response.getStatusCode());
            switch (responseCode) {
                case 0:
                    return updateTransactionInfo(response, PaymentStatus.PAID.toString());
                case -1:
                    errorMessage = "EINP42410001";
                    state = PaymentStatus.UNPAID.toString();
                    break;
                case 101:
                    errorMessage = "EINP42410002";
                    state = PaymentStatus.UNPAID.toString();
                    break;
                default:
                    errorMessage = "EINP50010002";
                    state = PaymentStatus.UNPAID.toString();
                    break;

            }
            updateTransactionInfo(response, state);
            throw new CodedException(ExceptionType.VerifyFailedCodedException, "E400010", errorMessage, response.getDescription());
        }
    }

    private Invoice updateTransactionInfo(PaymentVerificationResponse transactionInfo, String status) {
        Invoice existingInvoice = invoiceRepository.findByOrderId(transactionInfo.getOrderId()).get();
        if (transactionInfo == null) {
            existingInvoice.setPaymentStatus(PaymentStatus.getEnum(status));
        } else {
            existingInvoice.setPaymentStatus(PaymentStatus.getEnum(status));
            existingInvoice.setUpdateDateTime(new Timestamp(DateTime.now().getMillis()));
            existingInvoice.setTraceNumber(transactionInfo.getTraceNo());
            existingInvoice.setReferenceNumber(transactionInfo.getReferenceNo());
            existingInvoice.setRealTransactionDateTime(Timestamp.valueOf(transactionInfo.getTransactionDate()));
            existingInvoice.setCardNo(transactionInfo.getCardNo());
            existingInvoice.setHashedCardNo(transactionInfo.getHashedCardNo());
            existingInvoice.setTransactionDescription(transactionInfo.getDescription());
        }
        return invoiceRepository.saveAndFlush(existingInvoice);
    }

    public static PaymentVerificationResponse processVerifyResponse(GeneralVerificationResponse response) {
        return new PaymentVerificationResponse(response.getOrderId() != null ? Long.valueOf(response.getOrderId()) : null
                , response.getRetrivalRefNo(), response.getSystemTraceNo()
                , LocalDateTime.now(), response.getDescription(), Integer.valueOf(response.getResCode()), response.getHashedCardNo(), response.getCardNo());
    }

    public static String processGetTokenResponse(GeneralRegistrationResponse generalRegistrationResponse) throws TokenGenerationException {
        ResourceBundle resCodeResourceBundle = new ResourceBundleFactory().produceBundleWithLocale(Locale.ENGLISH, "ResponseCode");
        ResourceBundle switchResponseCodeBundle = new ResourceBundleFactory().produceBundleWithLocale(Locale.ENGLISH, "SwitchResponseCode");
        String resCode = generalRegistrationResponse.getResCode();
        String switchResCode = generalRegistrationResponse.getSwitchResCode();
        String responseMessage = resCode != null ? resCodeResourceBundle.getString(resCode) : null;
        String switchResponseMessage = (switchResCode == null || switchResCode.equals("")) ? null : switchResponseCodeBundle.getString(switchResCode);
        if (resCode.equals("0")) {
            return generalRegistrationResponse.getToken();
        }
        //todo log messages
        else {
            StringBuffer messages = new StringBuffer();
            messages.append("ResMsg:").append(responseMessage).append(". ").append("SwitchResMsg").append(switchResponseMessage);
            log.error(messages.toString());
            throw new TokenGenerationException(responseMessage, resCode);
        }
    }
}
