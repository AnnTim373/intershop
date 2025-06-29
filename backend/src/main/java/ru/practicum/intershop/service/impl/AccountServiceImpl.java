package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.PaymentClient;
import ru.practicum.intershop.client.model.BalanceOutputDTO;
import ru.practicum.intershop.service.AccountService;
import ru.practicum.intershop.service.UserService;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PaymentClient paymentClient;
    private final UserService userService;

    @Override
    public Mono<BalanceOutputDTO> getBalance(User user) {
        return userService.getUserInfo(user)
                .flatMap(userOutputDTO ->
                        paymentClient.getBalance(userOutputDTO.getId()).mapNotNull(HttpEntity::getBody)
                )
                .switchIfEmpty(Mono.empty());
    }

}
