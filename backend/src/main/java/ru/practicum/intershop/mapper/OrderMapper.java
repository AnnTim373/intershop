package ru.practicum.intershop.mapper;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Order;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;

public interface OrderMapper {

    Order fromDTO(OrderInputDTO orderInputDTO);

    Mono<OrderOutputDTO> toDTO(Order order);

}
