package ru.practicum.intershop.mapper;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.OrderContent;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;

import java.util.List;

public interface OrderContentMapper {

    Mono<List<OrderOutputDTO.OrderContentOutputDTO>> toDTO(Mono<List<OrderContent>> contentsMono);

    Mono<List<OrderContent>> fromDTO(Long orderId, List<OrderInputDTO.ContentDTO> contents);

}
