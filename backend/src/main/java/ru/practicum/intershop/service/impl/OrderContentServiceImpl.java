package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.OrderContent;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.mapper.OrderContentMapper;
import ru.practicum.intershop.repository.OrderContentRepository;
import ru.practicum.intershop.service.OrderContentService;
import ru.practicum.intershop.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderContentServiceImpl implements OrderContentService {

    private final OrderContentRepository orderContentRepository;

    private final ProductService productService;

    private final OrderContentMapper orderContentMapper;

    @Override
    public Mono<List<OrderContent>> findAllByOrderId(Long orderId) {
        Flux<OrderContent> orderContentFlux = orderContentRepository.findAllByOrderId(orderId);
        return orderContentFlux
                .map(orderContent -> {
                    orderContent.setProduct(productService.findById(orderContent.getProductId()));
                    return orderContent;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Mono<List<OrderContent>> save(Long orderId, List<OrderInputDTO.ContentDTO> contentDTOList) {
        return orderContentMapper.fromDTO(orderId, contentDTOList).flatMap(
                orderContents -> orderContentRepository.saveAll(orderContents)
                        .map(orderContent -> {
                            orderContent.setProduct(productService.findById(orderContent.getProductId()));
                            return orderContent;
                        })
                        .collectList()
        );
    }

}
