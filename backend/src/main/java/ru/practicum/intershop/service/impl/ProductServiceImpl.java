package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.mapper.ProductMapper;
import ru.practicum.intershop.repository.ProductRepository;
import ru.practicum.intershop.service.ProductService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductOutputDTO> findAll(Pageable pageable, String search) {
        if (search == null) {
            return productRepository.findAll(pageable).map(productMapper::toDTO);
        }
        return productRepository.findAllBySearch(pageable, search).map(productMapper::toDTO);
    }

    @Override
    public ProductOutputDTO save(ProductInputDTO productInputDTO) {
        return productMapper.toDTO(productRepository.save(productMapper.fromDTO(productInputDTO)));
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public boolean existsById(Long productId) {
        return productRepository.existsById(productId);
    }

}
