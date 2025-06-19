package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.PaymentClient;
import ru.practicum.intershop.client.api.PaymentApi;
import ru.practicum.intershop.client.model.PaymentInputDTO;
import ru.practicum.intershop.client.model.PaymentOutputDTO;
import ru.practicum.intershop.service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentClient paymentClient;

    @Override
    public Mono<PaymentOutputDTO> processPayment(PaymentInputDTO paymentInputDTO) {
        return paymentClient.processPayment(Mono.just(paymentInputDTO)).mapNotNull(HttpEntity::getBody);
    }

}
