package ru.practicum.intershop.validation;

import ru.practicum.intershop.dto.ProductInputDTO;

public interface ProductValidator {

    void validate(ProductInputDTO productInputDTO);

}
