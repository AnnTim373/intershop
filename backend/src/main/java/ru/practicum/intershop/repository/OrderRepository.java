package ru.practicum.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.intershop.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select o from Order o order by o.orderDateTime desc")
    List<Order> findAllOrderByOrderDateTimeDesc();

}
