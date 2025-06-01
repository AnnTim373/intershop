package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;
import ru.practicum.intershop.service.OrderService;
import ru.practicum.intershop.validation.OrderValidator;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderValidator orderValidator;

    @GetMapping
    public Mono<List<OrderOutputDTO>> getOrders() {
        return orderService.findAll();
    }

    @PostMapping
    public Mono<OrderOutputDTO> saveOrder(@RequestBody OrderInputDTO orderInputDTO) {
        orderValidator.validate(orderInputDTO);
        return orderService.createOrder(orderInputDTO);
    }

}
