package ru.practicum.intershop.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.OrderContent;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.dto.OrderOutputDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;
import ru.practicum.intershop.mapper.OrderContentMapper;
import ru.practicum.intershop.service.ProductService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderContentMapperImpl implements OrderContentMapper {

    private final ProductService productService;

    @Override
    public Mono<List<OrderOutputDTO.OrderContentOutputDTO>> toDTO(Mono<List<OrderContent>> contentsMono) {
        return contentsMono.flatMap(contents ->
                Flux.fromIterable(contents)
                        .flatMap(this::toDTO)
                        .collectList()
        );
    }

    @Override
    public Mono<List<OrderContent>> fromDTO(Long orderId, List<OrderInputDTO.ContentDTO> contents) {
        if (contents == null || contents.isEmpty()) throw new IntershopException("Нельзя создать пустой заказ");

        return Flux.fromIterable(contents)
                .flatMap(item -> productService.findById(item.getProductId())
                        .map(Product::getPrice)
                        .map(price -> OrderContent.builder()
                                .orderId(orderId)
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .productPriceFromOrder(price)
                                .build()))
                .collectList();
    }

    private Mono<OrderOutputDTO.OrderContentOutputDTO> toDTO(OrderContent content) {
        return content.getProduct().map(product -> OrderOutputDTO.OrderContentOutputDTO.builder()
                .productName(product.getName())
                .price(content.getProductPriceFromOrder())
                .quantity(content.getQuantity())
                .build());
    }

}
