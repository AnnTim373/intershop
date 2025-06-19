package ru.practicum.intershop.service;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.OrderContent;
import ru.practicum.intershop.dto.OrderInputDTO;

import java.util.List;

public interface OrderContentService {

    Mono<List<OrderContent>> findAllByOrderId(Long orderId);

    Mono<List<OrderContent>> save(Long orderId, List<OrderInputDTO.ContentDTO> contentDTOList);

}
