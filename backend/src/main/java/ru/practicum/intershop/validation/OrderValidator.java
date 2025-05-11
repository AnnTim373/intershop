package ru.practicum.intershop.validation;

import ru.practicum.intershop.dto.OrderInputDTO;

public interface OrderValidator {

    void validate(OrderInputDTO orderInputDTO);

}
