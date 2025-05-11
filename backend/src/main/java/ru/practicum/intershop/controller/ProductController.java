package ru.practicum.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.service.ProductService;
import ru.practicum.intershop.util.PaginationUtil;
import ru.practicum.intershop.validation.ProductValidator;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductValidator productValidator;

    @GetMapping("/products")
    public ResponseEntity<List<ProductOutputDTO>> getProducts(Pageable pageable,
                                                              @RequestParam(name = "search", required = false)
                                                              String search) {
        Page<ProductOutputDTO> page = productService.findAll(pageable, search);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductOutputDTO> saveProduct(@ModelAttribute ProductInputDTO productInputDTO) {
        productValidator.validate(productInputDTO);
        return ResponseEntity.ok(productService.save(productInputDTO));
    }

}
