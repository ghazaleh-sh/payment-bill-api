package ir.co.sadad.paymentBill.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.co.sadad.paymentBill.RequestParamVO;
import ir.co.sadad.paymentBill.dtos.GeneralRegistrationResponse;
import ir.co.sadad.paymentBill.dtos.GeneralVerificationResponse;
import ir.co.sadad.paymentBill.dtos.PaymentVerificationResDto;
import ir.co.sadad.paymentBill.exceptions.*;
import ir.co.sadad.paymentBill.dtos.PspInvoiceRegistrationReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGPaymentRequestResDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyReqDto;
import ir.co.sadad.paymentBill.dtos.ipg.IPGVerifyResDto;
import ir.co.sadad.paymentBill.services.basics.BasicWebClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.LocalDateTime;

/**
 * a service for making outer service calls to sadad.shaparak url directly and via ipg
 *
 * @author g.shahrokhabadi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SadadPspServiceImpl implements SadadPspService {

    private final BasicWebClient<PspInvoiceRegistrationReqDto, GeneralRegistrationResponse> registerInvoiceWebClient;
    private final BasicWebClient<Object, GeneralVerificationResponse> verifyInvoiceWebClient;

    private final BasicWebClient<IPGPaymentRequestReqDto, IPGPaymentRequestResDto> ipgRequestWebClient;
    private final BasicWebClient<IPGVerifyReqDto, IPGVerifyResDto> ipgVerifyWebClient;

    @Value(value = "${sadad.psp.base-url}")
    private String sadadBaseUrl;

    @Value(value = "${ipg.request_token.url}")
    private String ipgPaymentRequestUrl;

    @Value(value = "${ipg.verify_payment.url}")
    private String pspVerifyUrl;


    /**
     * old method for taking token directly from psp
     *
     * @param pspInvoiceRegistrationReq
     * @return
     */
    @Override
    public GeneralRegistrationResponse registerInvoiceByPsp(PspInvoiceRegistrationReqDto pspInvoiceRegistrationReq) {
        try {
            GeneralRegistrationResponse generalRegistrationResponse = registerInvoiceWebClient.doCallService(
                    pspInvoiceRegistrationReq,
                    sadadBaseUrl + "/TokenizedBillPaymentApi/BillRequest",
                    GeneralRegistrationResponse.class);

            if (generalRegistrationResponse.getResCode().equals("0")) {
                return generalRegistrationResponse;
            } else
                throw new BillPaymentException(generalRegistrationResponse.getDescription(), Integer.valueOf(generalRegistrationResponse.getResCode()), HttpStatus.BAD_REQUEST);

        } catch (ServiceUnavailableException e) {
            throw new ServiceUnavailableException("payment.request.unavailable");
        } catch (WebClientRequestException eWeb) {
            throw new MyWebClientRequestException(eWeb.getMessage());
        }

    }

    /**
     * old method for verifying bill payment by psp
     *
     * @param token    is psp token received by BillRequest call
     * @param signData
     * @param orderId
     * @return
     */
    @Override
    public PaymentVerificationResDto verifyInvoiceByPsp(String token, String signData, String orderId) {
        RequestParamVO requestParamVo = RequestParamVO.of(token, signData, orderId);

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
        return convertVerifyResToPaymentVerifyRes(generalVerificationResponse);
    }

    /**
     * sends request to ipg for bill payment and gets psp token
     *
     * @param ipgPaymentRequestReqDto
     * @param oauthToken
     * @return
     */
    @Override
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

    /**
     * verifies bill payment through ipg
     *
     * @param ipgVerifyReqDto Dto for sending request to verify payment
     * @return
     */
    @Override
    @SneakyThrows
    public PaymentVerificationResDto verifyBillPaymentByIpg(IPGVerifyReqDto ipgVerifyReqDto) {
        try {
            IPGVerifyResDto ipgVerifyResDto = ipgVerifyWebClient.doCallService(
                    ipgVerifyReqDto,
                    pspVerifyUrl,
                    IPGVerifyResDto.class);

            if (ipgVerifyResDto != null)
                switch (ipgVerifyResDto.getResCode()) {
                    case 0:
                        return convertIpgVerifyResToPaymentVerifyRes(ipgVerifyResDto);
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

    private static PaymentVerificationResDto convertIpgVerifyResToPaymentVerifyRes(IPGVerifyResDto response) {
        return PaymentVerificationResDto.builder()
                .orderId(response.getOrderId())
                .referenceNo(response.getRetrievalRefNo())
                .traceNo(response.getSystemTraceNo())
                .transactionDate(LocalDateTime.now())
                .description(response.getDescription())
                .statusCode(response.getResCode())
                .hashedCardNo(response.getHashedCardNo())
                .cardNo(response.getCardNo())
                .build();
    }

    private static PaymentVerificationResDto convertVerifyResToPaymentVerifyRes(GeneralVerificationResponse response) {
        return PaymentVerificationResDto.builder()
                .orderId(Long.valueOf(response.getOrderId()))
                .referenceNo(response.getRetrivalRefNo())
                .traceNo(response.getSystemTraceNo())
                .transactionDate(LocalDateTime.now())
                .description(response.getDescription())
                .statusCode(Integer.valueOf(response.getResCode()))
                .hashedCardNo(response.getHashedCardNo())
                .cardNo(response.getCardNo())
                .build();
    }
}
