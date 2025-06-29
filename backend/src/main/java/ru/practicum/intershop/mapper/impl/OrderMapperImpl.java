package ru.practicum.intershop.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Order;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;
import ru.practicum.intershop.mapper.OrderContentMapper;
import ru.practicum.intershop.mapper.OrderMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final OrderContentMapper orderContentMapper;

    @Override
    public Order fromDTO(OrderInputDTO orderInputDTO, Long userId) {
        return Order.builder()
                .userId(userId)
                .orderDateTime(LocalDateTime.now())
                .build();
    }

    @Override
    public Mono<OrderOutputDTO> toDTO(Order order) {
        return orderContentMapper.toDTO(order.getContents())
                .map(orderContent ->
                        OrderOutputDTO.builder()
                                .id(order.getId())
                                .orderDate(order.getOrderDateTime())
                                .contents(orderContent)
                                .build());
    }

}
