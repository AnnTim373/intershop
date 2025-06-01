package ru.practicum.intershop.mapper;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;

import java.util.List;

public interface ProductMapper {

    List<ProductOutputDTO> toDTO(List<Product> products);

    ProductOutputDTO toDTO(Product product);

    Mono<Product> fromDTO(ProductInputDTO productInputDTO);

}
