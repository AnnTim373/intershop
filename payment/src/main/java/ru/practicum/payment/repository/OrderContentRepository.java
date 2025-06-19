package ru.practicum.payment.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.practicum.payment.domain.OrderContent;

public interface OrderContentRepository extends R2dbcRepository<OrderContent, Long> {

    Flux<OrderContent> findAllByOrderId(Long orderId);

}
