package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.mapper.OrderMapper;
import ru.practicum.intershop.repository.OrderRepository;
import ru.practicum.intershop.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public void createOrder(OrderInputDTO orderInputDTO) {
        orderRepository.save(orderMapper.fromDTO(orderInputDTO));
    }

}
