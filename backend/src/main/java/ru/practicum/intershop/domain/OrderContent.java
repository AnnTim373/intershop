package ru.practicum.intershop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "shop", name = "order_content")
@SequenceGenerator(schema = "shop", sequenceName = "seq_order_content", name = "seq_order_content", allocationSize = 1)
public class OrderContent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_content")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "product_price_from_order")
    private Double productPriceFromOrder;

}
