package ru.practicum.intershop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.intershop.dto.ProductOutputDTO;

public interface ProductService {

    Page<ProductOutputDTO> findAll(Pageable pageable, String search);

}
