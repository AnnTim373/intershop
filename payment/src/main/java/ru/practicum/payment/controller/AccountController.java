package ru.practicum.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.payment.dto.BalanceOutputDTO;
import ru.practicum.payment.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public Mono<ResponseEntity<BalanceOutputDTO>> getBalance(@PathVariable(name = "accountId") Long accountId, ServerWebExchange exchange) {
        return accountService.getCurrentBalance(accountId).map(ResponseEntity::ok);
    }

}
