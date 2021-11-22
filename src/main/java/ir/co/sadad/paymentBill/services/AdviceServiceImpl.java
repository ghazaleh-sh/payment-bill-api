package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.exceptions.TokenGenerationException;
import ir.co.sadad.paymentBill.common.Encoder;
import ir.co.sadad.paymentBill.dtos.payment.*;
import ir.co.sadad.paymentBill.enums.ExceptionType;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.common.ResourceBundleFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;


@Slf4j
@RequiredArgsConstructor
@Service
public class AdviceServiceImpl implements AdviceApi {


    //    @Inject
//    @RestClient
//    private PspRestClient pspRestClient;
    private final Encoder encoder;
    private final SadadPspService sadadPspService;


    public PaymentVerificationResponse verifyTransaction(String token, String orderId) {
        return null;
    }

    @Override
    public PaymentVerificationResponse verifyPaymentTransaction(String token, String orderId) {
        if (token == null || token.isEmpty()) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E400005", "EINP40010009");
        }
        String base64SignedData = encoder.prepareSignDataWithToken(token);
        GeneralVerificationResponse generalVerificationResponse = sadadPspService.verifyPayment(token, base64SignedData, orderId);
        return processVerifyResponse(generalVerificationResponse);
    }

    @Override
    public String getPaymentToken(PaymentRegistration paymentRegistration) throws TokenGenerationException {
        PspPaymentRegistrationRegistrationRequest pspPaymentRegistrationRequest = preparePaymentRegsitrationRequest(paymentRegistration);
        GeneralRegistrationResponse generalRegistrationResponse = sadadPspService.registerPayment(pspPaymentRegistrationRequest);
        return processGetTokenResponse(generalRegistrationResponse);
    }

//    public PspInvoiceRegistrationRegistrationDtoRequset prepareInvoiceRegistrationRequest(InvoiceRegistrationDto invoiceRegistrationDto) {
//        String signData = encoder.prepareSignDataForRegistration(invoiceRegistrationDto.getTerminalId(), String.valueOf(invoiceRegistrationDto.getOrderId()), invoiceRegistrationDto.getAmount());
//        return new PspInvoiceRegistrationRegistrationDtoRequset.Builder()
//                .addAlreadyExistRegisterInvoice(invoiceRegistrationDto)
//                .signData(signData)
//                .build();
//    }

    public PspPaymentRegistrationRegistrationRequest preparePaymentRegsitrationRequest(PaymentRegistration paymentRegistration) {
        String signData = encoder.prepareSignDataForRegistration(paymentRegistration.getTerminalId(), String.valueOf(paymentRegistration.getOrderId()), paymentRegistration.getAmount());
        return new PspPaymentRegistrationRegistrationRequest.Builder()
                .addAlreadyExistRegisteredPayment(paymentRegistration)
                .signData(signData)
                .build();
    }

    /**
     * Advice process
     * @param response
     * @return
     */

    public static PaymentVerificationResponse processVerifyResponse(GeneralVerificationResponse response) {
        return new PaymentVerificationResponse(response.getOrderId() != null ? Long.valueOf(response.getOrderId()) : null
                , response.getRetrivalRefNo(), response.getSystemTraceNo()
                , LocalDateTime.now(), response.getDescription(), Integer.valueOf(response.getResCode()),response.getHashedCardNo(),response.getCardNo());
    }

    public static String processGetTokenResponse(GeneralRegistrationResponse generalRegistrationResponse) throws TokenGenerationException {
        ResourceBundle resCodeResourceBundle = new ResourceBundleFactory().produceBundleWithLocale(Locale.ENGLISH, "ResponseCode");
        ResourceBundle switchResponseCodeBundle = new ResourceBundleFactory().produceBundleWithLocale(Locale.ENGLISH, "SwitchResponseCode");
        String resCode = generalRegistrationResponse.getResCode();
        String switchResCode = generalRegistrationResponse.getSwitchResCode();
        String responseMessage = resCode != null ? resCodeResourceBundle.getString(resCode) : null;
        String switchResponseMessage = (switchResCode == null || switchResCode.equals("")) ?  null : switchResponseCodeBundle.getString(switchResCode);
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
