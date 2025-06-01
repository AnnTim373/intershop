package ru.practicum.intershop.validation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;
import ru.practicum.intershop.service.ProductService;
import ru.practicum.intershop.validation.OrderValidator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderValidatorImpl implements OrderValidator {

    private final ProductService productService;

    @Override
    public void validate(OrderInputDTO orderInputDTO) {
        if (orderInputDTO == null || orderInputDTO.getItems() == null || orderInputDTO.getItems().isEmpty())
            throw new IntershopException("Заказ не может быть null");
        checkItems(orderInputDTO.getItems());
    }

    private void checkItems(List<OrderInputDTO.ContentDTO> contents) {
        contents.forEach(item -> {
            checkItemProductId(item.getProductId());
            checkItemQuantity(item.getQuantity());
        });
    }

    private void checkItemProductId(Long productId) {
        if (productId == null) throw new IntershopException("Товар должен быть указан");
    }

    private void checkItemQuantity(Integer quantity) {
        if (quantity == null || quantity == 0) throw new IntershopException("Количество товара должно быть указано");
        if (quantity < 0) throw new IntershopException("Количество товара не может быть отрицательным");
    }

}
