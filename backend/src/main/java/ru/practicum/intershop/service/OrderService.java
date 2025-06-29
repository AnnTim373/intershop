package ru.practicum.intershop.service;

import org.springframework.security.core.userdetails.User;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;

import java.util.List;

public interface OrderService {

    Mono<OrderOutputDTO> createOrder(OrderInputDTO orderInputDTO, User user);

    Mono<List<OrderOutputDTO>> findAll(User user);

}
