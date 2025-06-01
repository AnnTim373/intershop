package ru.practicum.intershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.AbstractTestContainer;
import ru.practicum.intershop.TestContext;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.repository.OrderContentRepository;
import ru.practicum.intershop.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Import(TestContext.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ProductControllerIntegrationTest extends AbstractTestContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderContentRepository orderContentRepository;

    @Test
    void shouldSaveProduct() {
        getProducts().forEach(product -> webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products")
                        .queryParam("name", product.getName())
                        .queryParam("price", product.getPrice())
                        .queryParam("description", product.getDescription())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.description").isEqualTo(product.getDescription())
                .jsonPath("$.price").isEqualTo(product.getPrice()));
    }

    @Test
    void shouldReturnPaginatedProducts() {
        orderContentRepository.deleteAll().block();
        productRepository.deleteAll().block();
        getProducts().forEach(product ->
                productRepository.save(toEntity(product)).block()
        );

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("X-Total-Count")
                .expectBodyList(Object.class)
                .hasSize(6);
    }

    @Test
    void shouldFailOnValidation() {
        ProductInputDTO invalidProduct = new ProductInputDTO(
                "", "Test description", 100.0, null
        );

        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidProduct), ProductInputDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errorMessage").isEqualTo("Название товара должно быть заполнено");
    }

    private static List<ProductInputDTO> getProducts() {
        return Arrays.asList(
                new ProductInputDTO("Test product 1", "Test description 1", 100.0, null),
                new ProductInputDTO("Test product 2", "Test description 2", 200.0, null),
                new ProductInputDTO("Test product 3", "Test description 3", 300.0, null),
                new ProductInputDTO("Test product 4", "Test description 4", 400.0, null),
                new ProductInputDTO("Test product 5", "Test description 5", 500.0, null),
                new ProductInputDTO("Test product 6", "Test description 6", 600.0, null)
        );
    }

    public Product toEntity(ProductInputDTO input) {
        Product p = new Product();
        p.setName(input.getName());
        p.setDescription(input.getDescription());
        p.setPrice(input.getPrice());
        return p;
    }

}
