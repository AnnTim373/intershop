package ru.practicum.intershop.mapper;

import ru.practicum.intershop.domain.Order;
import ru.practicum.intershop.dto.OrderInputDTO;

public interface OrderMapper {

    Order fromDTO(OrderInputDTO orderInputDTO);

}
