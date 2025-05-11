package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.service.OrderService;
import ru.practicum.intershop.validation.OrderValidator;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderValidator orderValidator;

    @PostMapping
    public void saveOrder(@RequestBody OrderInputDTO orderInputDTO) {
        orderValidator.validate(orderInputDTO);
        orderService.createOrder(orderInputDTO);
    }

}
