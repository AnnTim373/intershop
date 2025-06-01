package ru.practicum.intershop.service;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.util.Page;
import ru.practicum.intershop.util.Pageable;

public interface ProductService {

    Mono<Page<ProductOutputDTO>> findAll(Pageable pageable, String search);

    Mono<ProductOutputDTO> save(ProductInputDTO productInputDTO);

    Mono<Product> findById(Long productId);

}
