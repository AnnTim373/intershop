package ru.practicum.payment.service;

import reactor.core.publisher.Flux;
import ru.practicum.payment.domain.OrderContent;

public interface OrderContentService {

    Flux<OrderContent> getOrderContents(Long orderId);

}
