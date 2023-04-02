package toss.payment.test.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import toss.payment.test.payment.dto.PaymentReq;
import toss.payment.test.payment.dto.SimplePaymentResponse;

import java.util.Base64;


@Service
@Transactional
@Slf4j
public class PaymentService {

    private static final String AUTHORIZATION = "Authorization";
    private final String secret;
    private final WebClient client;


    public PaymentService(@Value("${payment.key.secret}") String secret) {
        this.secret = secret;
        this.client = WebClient.builder()
                .baseUrl("https://api.tosspayments.com")
                .build();
    }

    public SimplePaymentResponse paymentTest(final PaymentReq paymentReq) throws Exception {
        ObjectMapper om = new ObjectMapper();
        String reqJSON = om.writeValueAsString(paymentReq);

        Mono<SimplePaymentResponse> mono = client.post()
                .uri("/v1/payments/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, getBasicKey())
                .body(BodyInserters.fromValue(reqJSON))
                .retrieve()
                .bodyToMono(SimplePaymentResponse.class);

        SimplePaymentResponse response = mono.block();

        return response;
    }



    public String getBasicKey() {
        String basic = "Basic ";
        String encodeKey = Base64.getEncoder().encodeToString(secret.getBytes());
        return basic + encodeKey;
    }
}
