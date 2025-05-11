package ru.practicum.intershop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "shop", name = "product")
@SequenceGenerator(schema = "shop", sequenceName = "seq_product", name = "seq_product", allocationSize = 1)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "image")
    private byte[] image;

}
