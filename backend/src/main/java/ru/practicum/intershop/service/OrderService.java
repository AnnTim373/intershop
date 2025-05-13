package ru.practicum.intershop.service;

import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;

import java.util.List;

public interface OrderService {

    OrderOutputDTO createOrder(OrderInputDTO orderInputDTO);

    List<OrderOutputDTO> findAll();

}
