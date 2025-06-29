package ru.practicum.intershop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.UserLoginDTO;
import ru.practicum.intershop.dto.UserOutputDTO;
import ru.practicum.intershop.dto.UserSignUpDTO;

public interface UserService {

    void save(UserSignUpDTO userSignUpDTO);

    Mono<Void> auth(UserLoginDTO userLoginDTO, ServerWebExchange exchange);

    Mono<UserOutputDTO> getUserInfo(User user);

}
