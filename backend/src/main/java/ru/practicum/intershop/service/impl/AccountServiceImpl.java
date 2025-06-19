package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.PaymentClient;
import ru.practicum.intershop.client.model.BalanceOutputDTO;
import ru.practicum.intershop.service.AccountService;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PaymentClient paymentClient;

    @Override
    public Mono<BalanceOutputDTO> getBalance(Long accountId) {
        return paymentClient.getBalance(accountId).mapNotNull(HttpEntity::getBody);
    }

}
