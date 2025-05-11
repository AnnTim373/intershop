package ru.practicum.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.intershop.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
