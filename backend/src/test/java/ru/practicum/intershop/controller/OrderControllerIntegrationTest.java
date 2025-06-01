package ru.practicum.intershop.controller;

import org.junit.jupiter.api.BeforeEach;
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
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.repository.ProductRepository;

import java.util.List;

@SpringBootTest
@Import(TestContext.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class OrderControllerIntegrationTest extends AbstractTestContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product = productRepository.save(product).block();
    }

    @Test
    void shouldSaveOrder() {
        OrderInputDTO order = getOrder();

        webTestClient.post()
                .uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), OrderInputDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.orderDate").isNotEmpty();
    }

    @Test
    void shouldReturnAllOrders() {
        webTestClient.get()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.size()").isEqualTo(1);
    }

    private OrderInputDTO getOrder() {
        OrderInputDTO order = new OrderInputDTO();
        order.setItems(List.of(new OrderInputDTO.ContentDTO(product.getId(), 4)));
        return order;
    }

}
