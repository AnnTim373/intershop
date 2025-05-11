package ru.practicum.intershop.service;

import ru.practicum.intershop.dto.OrderInputDTO;

public interface OrderService {

    void createOrder(OrderInputDTO orderInputDTO);

}
