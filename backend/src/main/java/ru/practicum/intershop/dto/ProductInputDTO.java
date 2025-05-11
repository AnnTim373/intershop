package ru.practicum.intershop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductInputDTO {

    private String name;

    private String description;

    private Double price;

    private MultipartFile image;

}
