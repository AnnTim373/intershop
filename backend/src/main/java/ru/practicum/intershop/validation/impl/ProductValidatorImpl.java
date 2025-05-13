package ru.practicum.intershop.validation.impl;

import org.springframework.stereotype.Component;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;
import ru.practicum.intershop.validation.ProductValidator;

import javax.xml.bind.ValidationException;

@Component
public class ProductValidatorImpl implements ProductValidator {

    @Override
    public void validate(ProductInputDTO productInputDTO) {
        if (productInputDTO == null) throw new IntershopException("Товар не может быть null");

        checkName(productInputDTO.getName());
        checkPrice(productInputDTO.getPrice());

    }

    private void checkName(String name) {
        if (name == null || name.isEmpty()) throw new IntershopException("Название товара должно быть заполнено");
    }

    private void checkPrice(Double price) {
        if (price == null) throw new IntershopException("Цена товара должна быть указана");
    }

}
