package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.UserLoginDTO;
import ru.practicum.intershop.dto.UserOutputDTO;
import ru.practicum.intershop.dto.UserSignUpDTO;
import ru.practicum.intershop.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserSignUpDTO userSignUpDTO) {
        userService.save(userSignUpDTO);
    }

    @PostMapping("/login")
    public Mono<Void> login(@RequestBody UserLoginDTO userLoginDTO, ServerWebExchange exchange) {
        return userService.auth(userLoginDTO, exchange);
    }

    @GetMapping("/account/me")
    public Mono<UserOutputDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getUserInfo(user);
    }

}
