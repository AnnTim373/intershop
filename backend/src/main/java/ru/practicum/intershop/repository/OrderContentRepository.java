package ru.practicum.intershop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.OrderContent;

import java.util.List;

public interface OrderContentRepository extends R2dbcRepository<OrderContent, Long> {

    Flux<OrderContent> findAllByOrderId(Long orderId);

}
