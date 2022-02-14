package ir.co.sadad.paymentBill.commons;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

/**
 * config of web client for rest services
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 200000)
                .wiretap(this.getClass().getCanonicalName(), LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(20000))
                                .addHandlerLast(new WriteTimeoutHandler(210000)));

        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
