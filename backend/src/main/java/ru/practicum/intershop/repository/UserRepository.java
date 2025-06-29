package ru.practicum.intershop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.User;

public interface UserRepository extends R2dbcRepository<User, Integer> {

    Mono<User> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

}
