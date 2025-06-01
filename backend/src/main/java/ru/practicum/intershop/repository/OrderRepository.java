package ru.practicum.intershop.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.practicum.intershop.domain.Order;

public interface OrderRepository extends R2dbcRepository<Order, Integer> {

    @Query(value = "select o.* from shop.order o order by o.order_date_time desc")
    Flux<Order> findAllOrderByOrderDateTimeDesc();

}
