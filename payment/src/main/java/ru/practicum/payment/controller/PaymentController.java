package ru.practicum.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.payment.dto.BalanceOutputDTO;
import ru.practicum.payment.dto.PaymentInputDTO;
import ru.practicum.payment.dto.PaymentOutputDTO;
import ru.practicum.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    @Override
    public Mono<ResponseEntity<PaymentOutputDTO>> processPayment(@RequestBody Mono<PaymentInputDTO> dto,
                                                                 ServerWebExchange exchange) {
        return paymentService.processPayment(dto).map(ResponseEntity::ok);
    }

}
