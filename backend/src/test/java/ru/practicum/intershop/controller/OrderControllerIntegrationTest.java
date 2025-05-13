package ru.practicum.intershop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.intershop.AbstractTestContainer;
import ru.practicum.intershop.TestContext;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.repository.ProductRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Import(TestContext.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIntegrationTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    private final Product product = new Product();

    @Test
    void shouldSaveOrder() throws Exception {
        OrderInputDTO order = getOrder();
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderDate").isNotEmpty())
                .andExpect(jsonPath("$.totalPrice").value(400.0));
    }

    @Test
    void shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].totalPrice").isNotEmpty());
    }


    private OrderInputDTO getOrder() {
        OrderInputDTO order = new OrderInputDTO();
        if (productRepository.findAll().isEmpty()) {
            product.setName("Test Product");
            product.setPrice(100.0);
            Product saved = productRepository.save(product);
            order.setItems(List.of(
                    new OrderInputDTO.Item(saved.getId(), 4)
            ));
        } else order.setItems(List.of(
                new OrderInputDTO.Item(productRepository.findById(1L).orElse(new Product()).getId(), 4)
        ));

        return order;
    }

}
