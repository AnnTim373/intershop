package ru.practicum.intershop.service;

import org.springframework.security.core.userdetails.User;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.model.BalanceOutputDTO;

public interface AccountService {

    Mono<BalanceOutputDTO> getBalance(User user);

}
