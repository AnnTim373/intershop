package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.model.BalanceOutputDTO;
import ru.practicum.intershop.service.AccountService;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}/balance")
    Mono<BalanceOutputDTO> getBalance(@PathVariable(name = "accountId") Long accountId) {
        return accountService.getBalance(accountId);
    }

}
