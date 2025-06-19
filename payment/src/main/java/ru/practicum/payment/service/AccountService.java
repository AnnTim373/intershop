package ru.practicum.payment.service;

import reactor.core.publisher.Mono;
import ru.practicum.payment.domain.Account;
import ru.practicum.payment.dto.BalanceOutputDTO;

public interface AccountService {

    Mono<BalanceOutputDTO> getCurrentBalance(Long accountId);

    Mono<Account> findAccountById(Long accountId);

    void save(Account account);

}
