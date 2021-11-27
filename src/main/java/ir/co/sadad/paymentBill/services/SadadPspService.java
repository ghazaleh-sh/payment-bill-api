package ir.co.sadad.paymentBill.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.co.sadad.paymentBill.RequestParamVO;
import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.dtos.GeneralVerificationResponse;
import ir.co.sadad.paymentBill.exceptions.GlobalErrorResponse;
import ir.co.sadad.paymentBill.dtos.PspInvoiceRegistrationReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestResDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyResDto;
import ir.co.sadad.paymentBill.dtos.payment.*;
import ir.co.sadad.paymentBill.enums.ExceptionType;
import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.exceptions.ServiceUnavailableException;
import ir.co.sadad.paymentBill.services.basics.BasicWebClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SadadPspService {

    private final BasicWebClient<PspInvoiceRegistrationReqDto, GeneralRegistrationResponse> registerInvoiceWebClient;
    private final BasicWebClient<Object, GeneralVerificationResponse> verifyInvoiceWebClient;
    private final BasicWebClient<PspPaymentRegistrationRegistrationRequest, GeneralRegistrationResponse> registerPaymentWebClient;
    private final BasicWebClient<Object, GeneralVerificationResponse> verifyPaymentWebClient;

    private final BasicWebClient<IPGPaymentRequestReqDto, IPGPaymentRequestResDto> ipgRequestWebClient;
    private final BasicWebClient<IPGVerifyReqDto, IPGVerifyResDto> ipgVerifyWebClient;

    @Value(value = "${sadad.psp.base-url}")
    private String sadadBaseUrl;

    @Value(value = "${ipg.request_token.url}")
    private String ipgPaymentRequestUrl;

    @Value(value = "${ipg.verify_payment.url}")
    private String pspVerifyUrl;


    public GeneralRegistrationResponse registerInvoiceByPsp(PspInvoiceRegistrationReqDto pspInvoiceRegistrationReq) {
        try {
            GeneralRegistrationResponse generalRegistrationResponse = registerInvoiceWebClient.doCallService(
                    pspInvoiceRegistrationReq,
                    sadadBaseUrl + "/TokenizedBillPaymentApi/BillRequest",
                    GeneralRegistrationResponse.class);

//            if (generalRegistrationResponse.getResCode() == "0") {
//                return generalRegistrationResponse;
//            }
//            //TODO:replace 502 with correct value
//            if (generalRegistrationResponse.getResCode() == "502") {
//                throw new CodedException(ExceptionType.PaymentAPIConnectionException, "E5000001", "EINP50010003");
//            }

            if (generalRegistrationResponse != null)
                return generalRegistrationResponse;
            else
                throw new ServiceUnavailableException("payment.request.unavailable");
//
        } catch (Exception e) {
            throw new CodedException(ExceptionType.PaymentAPIConnectionException, "E5000002", "EINP50010003");
        }

    }

    public GeneralRegistrationResponse registerPayment(PspPaymentRegistrationRegistrationRequest pspPaymentRegistrationRequest) {
        GeneralRegistrationResponse generalRegistrationResponse = registerPaymentWebClient.doCallService(
                pspPaymentRegistrationRequest,
                sadadBaseUrl + "/Request/PaymentRequest",
                GeneralRegistrationResponse.class);

        return generalRegistrationResponse;
    }

    public GeneralVerificationResponse verifyPayment(String token, String signData, String orderId) {
        RequestParamVO requestParamVo = RequestParamVO.of(token, signData ,orderId);
        GeneralVerificationResponse generalVerificationResponse = verifyPaymentWebClient.doCallService(
                requestParamVo,
                sadadBaseUrl +"/Advice/Verify",
                GeneralVerificationResponse.class);
//        return client.target(baseUrl)
//                .path("/Advice/Verify")
//                .queryParam("Token", token)
//                .queryParam("SignData", signData)
////                .queryParam("orderId", orderId)
//                .request()
//                .post(Entity.json(null)).readEntity(GeneralVerificationResponse.class);

        return generalVerificationResponse;
    }

    public GeneralVerificationResponse verifyInvoiceByPsp(String token, String signData, String orderId) {
        RequestParamVO requestParamVo = RequestParamVO.of(token, signData ,orderId);

        GeneralVerificationResponse generalVerificationResponse = verifyInvoiceWebClient.doCallService(
                requestParamVo,
                sadadBaseUrl + "/TokenizedBillPaymentApi/BillVerify",
                GeneralVerificationResponse.class);

//        String response = client.target(baseUrl)
//                .path("/TokenizedBillPaymentApi/BillVerify")
//                .queryParam("Token", token)
//                .queryParam("SignData", signData)
////                .queryParam("orderId", orderId)
//                .request()
//                .accept(MediaType.APPLICATION_JSON)
//                .post(Entity.json(request)).readEntity(String.class);

        generalVerificationResponse.setOrderId(orderId);
        return generalVerificationResponse;
    }

    @SneakyThrows
    public String requestPaymentByIpg(IPGPaymentRequestReqDto ipgPaymentRequestReqDto, String oauthToken) {

        try {
            IPGPaymentRequestResDto ipgPaymentRequestResDto = ipgRequestWebClient.doCallService(
                    ipgPaymentRequestReqDto,
                    oauthToken,
                    ipgPaymentRequestUrl,
                    IPGPaymentRequestResDto.class);

            if (ipgPaymentRequestResDto != null && !ipgPaymentRequestResDto.getToken().isEmpty())
                return ipgPaymentRequestResDto.getToken();

            else
                throw new ServiceUnavailableException("payment.request.unavailable");

        } catch (HttpStatusCodeException e) {

            log.error("IPG ERROR IS >>>>>>>>> " + e);
            GlobalErrorResponse globalErrorResponse =
                    new ObjectMapper().readValue(e.getResponseBodyAsString(), GlobalErrorResponse.class);
            throw new BillPaymentException(globalErrorResponse, e.getStatusCode());

        }
    }

    @SneakyThrows
    public GeneralVerificationResponse verifyBillPaymentByIpg(IPGVerifyReqDto ipgVerifyReqDto) {
        try {
            IPGVerifyResDto ipgVerifyResDto = ipgVerifyWebClient.doCallService(
                    ipgVerifyReqDto,
                    pspVerifyUrl,
                    IPGVerifyResDto.class);

            if (ipgVerifyResDto != null)
                switch (ipgVerifyResDto.getResCode()) {
                    case 0:
                        return convertIpgVerifyResToGeneralVerifyRes(ipgVerifyResDto);
                    case -1:
                        throw new ServiceUnavailableException("psp.verify.invalid.req");
                    case 101:
                        throw new ServiceUnavailableException("psp.verify.timeout");
                    default:
                        throw new BillPaymentException("psp.verify.unknown.res.code", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            else
                throw new ServiceUnavailableException("psp.verify.unavailable");

        } catch (HttpStatusCodeException e) {

            log.error("IPG ERROR IS >>>>>>>>> " + e);
            GlobalErrorResponse globalErrorResponse =
                    new ObjectMapper().readValue(e.getResponseBodyAsString(), GlobalErrorResponse.class);
            throw new BillPaymentException(globalErrorResponse, e.getStatusCode());

        }
    }

    private GeneralVerificationResponse convertIpgVerifyResToGeneralVerifyRes(IPGVerifyResDto ipgVerifyResDto){

        GeneralVerificationResponse generalVerifyRes = new GeneralVerificationResponse();
        generalVerifyRes.setAmount(ipgVerifyResDto.getAmount().toString());
        generalVerifyRes.setOrderId(ipgVerifyResDto.getOrderId().toString());
        generalVerifyRes.setCardNo(ipgVerifyResDto.getCardNo());
        generalVerifyRes.setDescription(ipgVerifyResDto.getDescription());
        generalVerifyRes.setHashedCardNo(ipgVerifyResDto.getHashedCardNo());
        generalVerifyRes.setResCode(ipgVerifyResDto.getResCode().toString());
        generalVerifyRes.setRetrivalRefNo(ipgVerifyResDto.getRetrievalRefNo());
        generalVerifyRes.setSwitchResCode(ipgVerifyResDto.getSwitchResCode());
        generalVerifyRes.setSystemTraceNo(ipgVerifyResDto.getSystemTraceNo());
        return  generalVerifyRes;
    }
}
