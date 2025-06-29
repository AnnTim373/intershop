package ru.practicum.intershop.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
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
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    @Value("${app.payment-url}")
    private String baseUrl;


    @Override
    public Mono<ResponseEntity<PaymentOutputDTO>> processPayment(Mono<PaymentInputDTO> dto) {
        return getOAuthToken().flatMap(token -> webClient.post()
                .uri(baseUrl + "/api/payment")
                .header("Authorization", "Bearer " + token)
                .body(dto, PaymentInputDTO.class)
                .retrieve()
                .toEntity(PaymentOutputDTO.class));
    }

    @Override
    public Mono<ResponseEntity<BalanceOutputDTO>> getBalance(Long userId) {
        return getOAuthToken().flatMap(token -> webClient.get()
                .uri(baseUrl + "/api/account/" + userId + "/balance")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toEntity(BalanceOutputDTO.class));
    }

    private Mono<String> getOAuthToken() {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal("intershop-client")
                .build();

        Mono<OAuth2AuthorizedClient> clientMono = authorizedClientManager.authorize(request);
        return clientMono.map(client -> client.getAccessToken().getTokenValue());
    }

}
