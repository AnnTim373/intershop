package ru.practicum.intershop.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(schema = "shop", name = "product")
public class Product {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "name")
    private String name;

    @Column(value = "description")
    private String description;

    @Column(value = "price")
    private Double price;

    @Column(value = "image")
    private byte[] image;

}
