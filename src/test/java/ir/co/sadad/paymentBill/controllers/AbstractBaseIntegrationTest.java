package ir.co.sadad.paymentBill.controllers;


import ir.co.sadad.paymentBill.commons.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@ActiveProfiles(profiles = {"qa"})
public abstract class AbstractBaseIntegrationTest {

    protected WebTestClient webTestClient;
    protected TokenDto clientToken;
    protected UserVO userVO;

    protected String userId = "158";
    protected String ssn = "0079993141";
    protected String cellphone = "9124150188";
    protected String deviceId = "5700cd58-3cd6-4ce3-81ff-ee519e1f6df7";

    public AbstractBaseIntegrationTest() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8517/api/payment-bill")
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    @BeforeEach
    protected void initialize() {
        userVO = UserVO.of(userId, cellphone, deviceId, ssn);

        WebClient client = WebClient.builder()
                .baseUrl("http://185.135.30.10:9443")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        clientToken = client.post()
                .uri("/identity/oauth2/auth/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic a2V5OnNlY3JldA==")
                .body(BodyInserters.fromFormData("scope", "customer-super ipg-payment-verfiy")
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToFlux(TokenDto.class).blockLast();
    }

}