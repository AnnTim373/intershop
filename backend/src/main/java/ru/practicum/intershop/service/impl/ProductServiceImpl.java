package ru.practicum.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.mapper.ProductMapper;
import ru.practicum.intershop.repository.ProductRepository;
import ru.practicum.intershop.service.ProductService;
import ru.practicum.intershop.util.Page;
import ru.practicum.intershop.util.Pageable;
import ru.practicum.intershop.util.SortUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Mono<Page<ProductOutputDTO>> findAll(Pageable pageable, String search) {
        Mono<List<Product>> productsMono = search == null ?
                productRepository.findAll(
                        pageable.getSize(), pageable.getSize() * pageable.getPage(),
                        pageable.getNormalizeSort().toString()
                ).collectList() :
                productRepository.findAllBySearch(
                        pageable.getSize(), pageable.getSize() * pageable.getPage(),
                        pageable.getNormalizeSort().toString(),
                        search
                ).collectList();
        Mono<Long> countMono = search == null ? productRepository.count() : productRepository.countBySearch(search);

        Mono<Page<ProductOutputDTO>> pageMono = Mono.zip(productsMono.map(productMapper::toDTO), countMono,
                (content, count) -> new Page<>(content, pageable, count));
        return pageMono.map(page -> {
            SortUtils.sortByField(page.getContent(),
                    page.getPageable().getNormalizeSort().getColumn(),
                    page.getPageable().getNormalizeSort().getDirection());
            return page;
        });
    }

    @Override
    public Mono<ProductOutputDTO> save(ProductInputDTO productInputDTO) {
        Mono<Product> productMono = productMapper.fromDTO(productInputDTO);
        Mono<Product> savedMono = productMono.flatMap(productRepository::save);
        return savedMono.map(productMapper::toDTO);
    }

    @Override
    public Mono<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

}
