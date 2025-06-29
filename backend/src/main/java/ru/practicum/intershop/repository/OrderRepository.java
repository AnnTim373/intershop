package ru.practicum.intershop.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import ru.practicum.intershop.domain.Order;

public interface OrderRepository extends R2dbcRepository<Order, Integer> {

    @Query(value = "select o.* from shop.order o where o.user_id = :userId order by o.order_date_time desc")
    Flux<Order> findAllOrderByOrderDateTimeDesc(@Param("userId") Long userId);

}
