package ru.practicum.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.payment.domain.Account;
import ru.practicum.payment.dto.BalanceOutputDTO;
import ru.practicum.payment.repository.AccountRepository;
import ru.practicum.payment.service.AccountService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Mono<BalanceOutputDTO> getCurrentBalance(Long userId) {
        return accountRepository.findByUserId(userId)
                .map(account -> new BalanceOutputDTO().balance(account.getBalance().doubleValue()))
                .switchIfEmpty(accountRepository.save(
                                Account.builder()
                                        .userId(userId)
                                        .balance(BigDecimal.ZERO)
                                        .build())
                        .map(account -> new BalanceOutputDTO().balance(account.getBalance().doubleValue()))
                );
    }

    @Override
    public Mono<Account> findAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account).subscribe();
    }

}
