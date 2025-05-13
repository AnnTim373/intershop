package ru.practicum.intershop.mapper;

import ru.practicum.intershop.domain.Order;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;

import java.util.List;

public interface OrderMapper {

    Order fromDTO(OrderInputDTO orderInputDTO);

    List<OrderOutputDTO> toDTO(List<Order> orderList);

    OrderOutputDTO toDTO(Order order);

}
