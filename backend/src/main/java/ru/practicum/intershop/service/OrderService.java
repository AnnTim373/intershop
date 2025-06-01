package ru.practicum.intershop.service;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;

import java.util.List;

public interface OrderService {

    Mono<OrderOutputDTO> createOrder(OrderInputDTO orderInputDTO);

    Mono<List<OrderOutputDTO>> findAll();

}
