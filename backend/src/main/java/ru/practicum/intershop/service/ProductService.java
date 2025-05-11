package ru.practicum.intershop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;

import java.util.Optional;

public interface ProductService {

    Page<ProductOutputDTO> findAll(Pageable pageable, String search);

    ProductOutputDTO save(ProductInputDTO productInputDTO);

    Optional<Product> findById(Long productId);

    boolean existsById(Long productId);

}
