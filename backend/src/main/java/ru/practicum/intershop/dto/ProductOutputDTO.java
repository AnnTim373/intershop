package ru.practicum.intershop.dto;

import lombok.Data;

@Data
public class ProductOutputDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private String image;

}
