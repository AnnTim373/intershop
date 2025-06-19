package ru.practicum.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.payment.domain.Account;
import ru.practicum.payment.dto.BalanceOutputDTO;
import ru.practicum.payment.repository.AccountRepository;
import ru.practicum.payment.service.AccountService;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Mono<BalanceOutputDTO> getCurrentBalance(Long accountId) {
        return accountRepository.findById(accountId)
                .map(account -> new BalanceOutputDTO().balance(account.getBalance().doubleValue()));
    }

    @Override
    public Mono<Account> findAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account).subscribe();
    }

}
