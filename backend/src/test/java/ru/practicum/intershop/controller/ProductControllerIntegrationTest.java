package ru.practicum.intershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.intershop.AbstractTestContainer;
import ru.practicum.intershop.TestContext;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Import(TestContext.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveProduct() {
        getProducts().forEach(product -> {
            try {
                mockMvc.perform(multipart("/api/products")
                                .param("name", product.getName())
                                .param("description", product.getDescription())
                                .param("price", product.getPrice().toString())
                                .characterEncoding("UTF-8"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").isNotEmpty())
                        .andExpect(jsonPath("$.name").value(product.getName()))
                        .andExpect(jsonPath("$.description").value(product.getDescription()))
                        .andExpect(jsonPath("$.price").value(product.getPrice().toString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void shouldReturnPaginatedProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andExpect(jsonPath("$.size()").value(productRepository.findAll().size()));
    }

    @Test
    void shouldFailOnValidation() throws Exception {
        mockMvc.perform(multipart("/api/products")
                        .param("name", "")
                        .param("description", "Test description")
                        .param("price", "100.0")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("Название товара должно быть заполнено"));
    }

    private static List<ProductInputDTO> getProducts() {
        ProductInputDTO product1 = new ProductInputDTO("Test product 1",
                "Test description 1", 100.0, null);
        ProductInputDTO product2 = new ProductInputDTO("Test product 2",
                "Test description 2", 200.0, null);
        ProductInputDTO product3 = new ProductInputDTO("Test product 3",
                "Test description 3", 300.0, null);
        ProductInputDTO product4 = new ProductInputDTO("Test product 4",
                "Test description 4", 400.0, null);
        ProductInputDTO product5 = new ProductInputDTO("Test product 5",
                "Test description 5", 500.0, null);
        ProductInputDTO product6 = new ProductInputDTO("Test product 6",
                "Test description 6", 600.0, null);
        return Arrays.asList(product1, product2, product3, product4, product5, product6);
    }

}
