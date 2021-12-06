package ir.co.sadad.paymentBill.services.basics;

import ir.co.sadad.paymentBill.RequestParamVO;
import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.GlobalErrorResponse;
import ir.co.sadad.paymentBill.exceptions.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicWebClient<T, K> {

    private final WebClient webClient;

    public K doCallService(T body, String urlQueryString, Class<K> responseType) {

        try {
            K response = webClient
                    .post()
                    .uri(urlQueryString)
                    .body(Mono.just(body), new ParameterizedTypeReference<T>() {
                    })
                    .retrieve()
                    .bodyToMono(responseType)
                    .onErrorMap(e -> {
                        return new ServiceUnavailableException("service.unavailable");
                    })
                    .block();

            log.info("response of external service >>>>>>>>>>>>>>>>>> ", response);
            return response;
        } catch (RestClientException e) {
            log.error("ERROR IS EXTERNAL SERVICE>>>>>>>>>>>>>>>>>", e);
            return null;
//            throw new ServiceUnavailableException(serviceUnavailableMessage());

        }

    }

    public K doCallService(RequestParamVO requestParamVO, String urlQueryString, Class<K> responseType) {

        try {
            K response = webClient
                    .post()
                    .uri(builder -> builder.path(urlQueryString)
                            .queryParam("Token", requestParamVO.getToken())
                            .queryParam("SignData", requestParamVO.getSignData())
//                               .queryParam("orderId", requestParamVO.getOrderId())
                            .build())
//                    .body(BodyInserters.fromValue(request.body)), new ParameterizedTypeReference<T>() {
//                    })
                    .retrieve()
                    .bodyToMono(responseType)
                    .onErrorMap(e -> {
                        return new ServiceUnavailableException("service.unavailable");
                    })
                    .block();

            log.info("response of external service >>>>>>>>>>>>>>>>>> ", response);
            return response;
        } catch (RestClientException e) {


            log.error("ERROR IS EXTERNAL SERVICE>>>>>>>>>>>>>>>>>", e);
            throw new ServiceUnavailableException(e.getMessage());

        }

    }

    public K doCallService(T body, String headerToken, String urlQueryString, Class<K> responseType) {
        try {
            K response = webClient
                    .post()
                    .uri(urlQueryString)
                    .header(HttpHeaders.AUTHORIZATION, headerToken)
                    .body(Mono.just(body), new ParameterizedTypeReference<T>() {
                    })
                    .retrieve()
                    .onStatus(HttpStatus::isError, res -> res.bodyToMono(GlobalErrorResponse.class)
                            .onErrorResume(e -> Mono.error(new BillPaymentException("aaaa", HttpStatus.INTERNAL_SERVER_ERROR)))
                            .flatMap(errorBody -> Mono.error(new BillPaymentException(errorBody.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)))
                    )
                    .bodyToMono(responseType)
                    .block();

            log.info("response of external service >>>>>>>>>>>>>>>>>> ", response);
            return response;
        } catch (RestClientException e) {
            log.error("ERROR IS EXTERNAL SERVICE>>>>>>>>>>>>>>>>>", e);
            throw new ServiceUnavailableException(e.getMessage());

        }
    }
}
