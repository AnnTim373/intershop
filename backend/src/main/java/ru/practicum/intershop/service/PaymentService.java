package ru.practicum.intershop.service;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.model.PaymentInputDTO;
import ru.practicum.intershop.client.model.PaymentOutputDTO;

public interface PaymentService {

    Mono<PaymentOutputDTO> processPayment(PaymentInputDTO paymentInputDTO);

}
