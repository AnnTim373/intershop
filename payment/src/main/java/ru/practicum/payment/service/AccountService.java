package ru.practicum.payment.service;

import reactor.core.publisher.Mono;
import ru.practicum.payment.domain.Account;
import ru.practicum.payment.dto.BalanceOutputDTO;

public interface AccountService {

    Mono<BalanceOutputDTO> getCurrentBalance(Long userId);

    Mono<Account> findAccountByUserId(Long userId);

    void save(Account account);

}
