package ru.practicum.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.payment.domain.OrderContent;
import ru.practicum.payment.repository.OrderContentRepository;
import ru.practicum.payment.service.OrderContentService;

@Service
@RequiredArgsConstructor
public class OrderContentServiceImpl implements OrderContentService {

    private final OrderContentRepository orderContentRepository;

    @Override
    public Flux<OrderContent> getOrderContents(Long orderId) {
        return orderContentRepository.findAllByOrderId(orderId);
    }

}
