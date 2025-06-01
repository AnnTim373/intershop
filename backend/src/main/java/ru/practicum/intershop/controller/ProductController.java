package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.service.ProductService;
import ru.practicum.intershop.util.Page;
import ru.practicum.intershop.util.Pageable;
import ru.practicum.intershop.util.PaginationUtil;
import ru.practicum.intershop.validation.ProductValidator;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductValidator productValidator;

    @GetMapping
    public Mono<ResponseEntity<List<ProductOutputDTO>>> getProducts(Pageable pageable,
                                                                    @RequestParam(name = "search", required = false)
                                                                    String search) {
        Mono<Page<ProductOutputDTO>> pageMono = productService.findAll(pageable, search);
        return pageMono.map(page -> {
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);

            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        });
    }

    @PostMapping
    public Mono<ProductOutputDTO> saveProduct(@ModelAttribute ProductInputDTO productInputDTO) {
        log.info("Saving product {}", productInputDTO);
        productValidator.validate(productInputDTO);
        return productService.save(productInputDTO);
    }

}
