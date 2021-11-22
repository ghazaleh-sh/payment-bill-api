package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.UserVO;
import ir.co.sadad.paymentBill.dtos.InvoiceRequestDto;
import ir.co.sadad.paymentBill.dtos.InvoiceVerifyDto;
import ir.co.sadad.paymentBill.dtos.ipg.FinalBillPaymentResDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.payment.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.dtos.payment.GeneralVerificationResponse;
import ir.co.sadad.paymentBill.dtos.payment.PspInvoiceRegistrationReqDto;
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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

    private HttpServletRequest request;

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
    public InvoiceVerifyDto invoiceRegister(InvoiceRequestDto invoiceRequestDto) {

        String userId = "158";
        String cellPhone ="09218301631";
        String serialId = "5700cd58-3cd6-4ce3-81ff-ee519e1f6df7";
        String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJncmFudCI6IkNMSUVOVCIsImlzcyI6Imh0dHA6Ly9hcGkuYm1pLmlyL3NlY3VyaXR5IiwiYXVkIjoia2V5IiwiZXhwIjoxNjM3MTI5NjQxNzQxLCJuYmYiOjE2MzcwNDMyNDE3NDEsInJvbGUiOiIiLCJzZXJpYWwiOiIzYTY2YTFkMC00NTE1LTNhMjMtOTFmMS03NzYzMzA4NmI1OGUiLCJzc24iOiIxMjMiLCJjbGllbnRfaWQiOiIxMjMiLCJzY29wZXMiOlsiY3VzdG9tZXItc3VwZXIiXX0=.fNPgh_2w-r5mYzwhKN2tTyf_YOYQ6YV3GkEail6S3ck";
        String ssn = "0079993141";


        Invoice savedinvoice = invoiceCreation(invoiceRequestDto, UserVO.of(userId, cellPhone, serialId));

        PspInvoiceRegistrationReqDto pspInvoiceRegistrationReqDto = prepareInvoiceRegistration(savedinvoice);

        String signData = encoder.prepareSignDataForRegistration(pspInvoiceRegistrationReqDto.getTerminalId(), String.valueOf(pspInvoiceRegistrationReqDto.getOrderId()), pspInvoiceRegistrationReqDto.getAmount());
        pspInvoiceRegistrationReqDto.setSignData(signData);
        GeneralRegistrationResponse generalRegistrationResponse = sadadPspService.registerInvoiceByPsp(pspInvoiceRegistrationReqDto);
        String token = processGetTokenResponse(generalRegistrationResponse);

        InvoiceVerifyDto verifyResponse = new InvoiceVerifyDto();
        verifyResponse.setOrderId(String.valueOf(savedinvoice.getOrderId()));
        verifyResponse.setToken(token);
        return verifyResponse;
    }

    @Override
    public InvoiceVerifyDto BillPaymentByIpg(InvoiceRequestDto invoiceRequestDto, UserVO userVo, String authToken){

        Invoice savedinvoice = invoiceCreation(invoiceRequestDto, userVo);

        String pspToken = sadadPspService.requestPaymentByIpg(makeIpgPaymentRequest(savedinvoice,invoiceRequestDto,userVo) , authToken);

        InvoiceVerifyDto billPaymentResDto = new InvoiceVerifyDto();
        billPaymentResDto.setToken(pspToken);
        billPaymentResDto.setOrderId(savedinvoice.getOrderId().toString());
        return billPaymentResDto;

    }

    @Override
    public FinalBillPaymentResDto finalBillPaymentByIpg(InvoiceVerifyDto invoiceVerifyDto){
        //Validated before
        Optional<Invoice> singleResult = invoiceRepository.findByOrderId(Long.valueOf(invoiceVerifyDto.getOrderId()));

        GeneralVerificationResponse generalVerificationResponse = sadadPspService.verifyBillPaymentByIpg(makeIpgVerifyRequest(invoiceVerifyDto,singleResult.get().getUserId()));

        processVerifyResponse(processVerifyResponse(generalVerificationResponse));

        FinalBillPaymentResDto  finalResponse = new FinalBillPaymentResDto();
        finalResponse.setStatus(IpgVerificationStatus.SUCCESSFUL);
        return finalResponse;
    }

    private IPGPaymentRequestReqDto makeIpgPaymentRequest(Invoice savedinvoice, InvoiceRequestDto invoiceRequestDto, UserVO user){
        IPGPaymentRequestReqDto req = new IPGPaymentRequestReqDto();
        req.setAmount(Long.valueOf(invoiceRequestDto.getAmount()));
        req.setServiceType(PAYMENT_BILL_SERVICE_TYPE);
        req.setRequestId(savedinvoice.getOrderId().toString());
        req.setUserDeviceId(user.getSerialId());
        req.setUserId(user.getUserId());
        req.setSsn(user.getSsn());
        req.setInvoiceNumber(invoiceRequestDto.getInvoiceNumber());
        req.setPaymentNumber(invoiceRequestDto.getPaymentNumber());
        return req;
    }

    private IPGVerifyReqDto makeIpgVerifyRequest(InvoiceVerifyDto req, String userId) {
        IPGVerifyReqDto ipgVerifyReq = new IPGVerifyReqDto();
        ipgVerifyReq.setRequestId(req.getOrderId());
        ipgVerifyReq.setToken(req.getToken());
        ipgVerifyReq.setUserId(userId);
        ipgVerifyReq.setServiceType(PAYMENT_BILL_SERVICE_TYPE);
        return ipgVerifyReq;
    }

    private Invoice invoiceCreation(InvoiceRequestDto invoiceRequestDto, UserVO userVo){
        Optional<Invoice> invoice = invoiceRepository.findByInvoiceNumberAndPaymentNumber(invoiceRequestDto.getInvoiceNumber(), invoiceRequestDto.getPaymentNumber());
        Invoice savedinvoice = invoice.orElse(makeNewInvoice(invoiceRequestDto, userVo));

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

    private Invoice makeNewInvoice(InvoiceRequestDto invoiceRequestDto, UserVO userVo) {
        InvoiceType invoiceType = InvoiceType.getEnum(Integer.parseInt(invoiceRequestDto.getInvoiceNumber().substring(invoiceRequestDto.getInvoiceNumber().length() - 2, invoiceRequestDto.getInvoiceNumber().length() - 1)));

        Invoice savedInvoice = new Invoice();
        savedInvoice.setInvoiceNumber(invoiceRequestDto.getInvoiceNumber());
        savedInvoice.setPaymentNumber(invoiceRequestDto.getPaymentNumber());
        savedInvoice.setInvoiceType(invoiceType);
        savedInvoice.setAmount(new BigDecimal(invoiceRequestDto.getAmount()));
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
                .amount(String.valueOf(invoice.getAmount()))
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
