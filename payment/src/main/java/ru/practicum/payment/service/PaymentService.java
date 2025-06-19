package ru.practicum.payment.service;

import reactor.core.publisher.Mono;
import ru.practicum.payment.dto.PaymentInputDTO;
import ru.practicum.payment.dto.PaymentOutputDTO;

public interface PaymentService {

    Mono<PaymentOutputDTO> processPayment(Mono<PaymentInputDTO> dto);

}
