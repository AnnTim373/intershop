package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;
import ru.practicum.intershop.mapper.OrderMapper;
import ru.practicum.intershop.repository.OrderRepository;
import ru.practicum.intershop.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderOutputDTO createOrder(OrderInputDTO orderInputDTO) {
        return orderMapper.toDTO(orderRepository.save(orderMapper.fromDTO(orderInputDTO)));
    }

    @Override
    public List<OrderOutputDTO> findAll() {
        return orderMapper.toDTO(orderRepository.findAllOrderByOrderDateTimeDesc());
    }

}
