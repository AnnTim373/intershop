package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.UserLoginDTO;
import ru.practicum.intershop.dto.UserOutputDTO;
import ru.practicum.intershop.dto.UserSignUpDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;
import ru.practicum.intershop.mapper.UserMapper;
import ru.practicum.intershop.repository.UserRepository;
import ru.practicum.intershop.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository contextRepository;

    @Override
    public void save(UserSignUpDTO userSignUpDTO) {
        userRepository.existsByUsername(userSignUpDTO.username()).subscribe(existUser -> {
            if (existUser) {
                throw new IntershopException("Пользователь с таким логином уже существует");
            }
            userRepository.save(userMapper.fromDTO(userSignUpDTO)).subscribe();
        });
    }

    @Override
    public Mono<Void> auth(UserLoginDTO userLoginDTO, ServerWebExchange exchange) {
        if (userLoginDTO != null && userLoginDTO.username() != null && userLoginDTO.password() != null) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    userLoginDTO.username(), userLoginDTO.password()
            );
            return authenticationManager.authenticate(authRequest)
                    .flatMap(authentication ->
                            contextRepository.save(exchange, new SecurityContextImpl(authentication))
                    );
        }
        throw new IntershopException("Invalid username or password");
    }

    @Override
    public Mono<UserOutputDTO> getUserInfo(User user) {
        if (user == null) return Mono.just(anonymousUser());
        return userRepository.findByUsername(user.getUsername())
                .map(userFromDb -> UserOutputDTO.builder()
                        .id(userFromDb.getId())
                        .username(userFromDb.getUsername())
                        .role(UserOutputDTO.Role.USER)
                        .build()
                );
    }

    private UserOutputDTO anonymousUser() {
        return UserOutputDTO.builder()
                .id(0L)
                .username("anonymous")
                .role(UserOutputDTO.Role.ANONYMOUS)
                .build();
    }

}
