package ru.practicum.intershop.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.PaymentClient;
import ru.practicum.intershop.client.model.BalanceOutputDTO;
import ru.practicum.intershop.client.model.PaymentInputDTO;
import ru.practicum.intershop.client.model.PaymentOutputDTO;

@Component
@RequiredArgsConstructor
public class PaymentClientImpl implements PaymentClient {

    private final WebClient webClient;

    @Value("${app.payment-url}")
    private String baseUrl;


    @Override
    public Mono<ResponseEntity<PaymentOutputDTO>> processPayment(Mono<PaymentInputDTO> dto) {
        return webClient.post()
                .uri(baseUrl + "/api/payment")
                .body(dto, PaymentInputDTO.class)
                .retrieve()
                .toEntity(PaymentOutputDTO.class);
    }

    @Override
    public Mono<ResponseEntity<BalanceOutputDTO>> getBalance(Long accountId) {
        return webClient.get()
                .uri(baseUrl + "/api/account/" + accountId + "/balance")
                .retrieve()
                .toEntity(BalanceOutputDTO.class);
    }

}
