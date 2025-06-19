package ru.practicum.intershop.service;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.model.BalanceOutputDTO;

public interface AccountService {

    Mono<BalanceOutputDTO> getBalance(Long accountId);

}
