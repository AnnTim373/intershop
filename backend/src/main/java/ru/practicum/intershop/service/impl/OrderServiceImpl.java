package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.model.PaymentInputDTO;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;
import ru.practicum.intershop.dto.UserOutputDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;
import ru.practicum.intershop.mapper.OrderMapper;
import ru.practicum.intershop.repository.OrderRepository;
import ru.practicum.intershop.service.OrderContentService;
import ru.practicum.intershop.service.OrderService;
import ru.practicum.intershop.service.PaymentService;
import ru.practicum.intershop.service.UserService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderContentService orderContentService;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;
    private final UserService userService;

    @Override
    public Mono<OrderOutputDTO> createOrder(OrderInputDTO orderInputDTO, User user) {
        return userService.getUserInfo(user).flatMap(userOutputDTO ->
                orderRepository.save(orderMapper.fromDTO(orderInputDTO, userOutputDTO.getId()))
                        .flatMap(order ->
                                orderContentService.save(order.getId(), orderInputDTO.getItems())
                                        .flatMap(orderContents -> {
                                            order.setContents(Mono.just(orderContents));
                                            return paymentService.processPayment(
                                                    new PaymentInputDTO()
                                                            .orderId(order.getId())
                                                            .userId(userOutputDTO.getId())
                                            ).flatMap(paymentResponse -> {
                                                if (paymentResponse.getStatus() == true) {
                                                    return orderMapper.toDTO(order);
                                                } else {
                                                    return Mono.error(
                                                            new IntershopException(
                                                                    "Payment failed for order " + order.getId()
                                                            )
                                                    );
                                                }
                                            });
                                        }))).switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<List<OrderOutputDTO>> findAll(User user) {
        Mono<UserOutputDTO> userMono = userService.getUserInfo(user);
        return userMono.flatMap(userOutputDTO ->
                orderRepository.findAllOrderByOrderDateTimeDesc(userOutputDTO.getId())
                        .flatMap(order -> {
                            order.setContents(orderContentService.findAllByOrderId(order.getId()));
                            return orderMapper.toDTO(order);
                        }).collectList().map(orderList -> {
                            orderList.sort(Comparator.comparing(OrderOutputDTO::getOrderDate).reversed());
                            return orderList;
                        })).switchIfEmpty(Mono.empty());

    }

}
