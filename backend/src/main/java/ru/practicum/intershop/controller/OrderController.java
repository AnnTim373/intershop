package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<OrderOutputDTO>> getOrders() {
        List<OrderOutputDTO> orderList = orderService.findAll();
        return ResponseEntity.ok(orderList);
    }

    @PostMapping
    public OrderOutputDTO saveOrder(@RequestBody OrderInputDTO orderInputDTO) {
        orderValidator.validate(orderInputDTO);
        return orderService.createOrder(orderInputDTO);
    }

}
