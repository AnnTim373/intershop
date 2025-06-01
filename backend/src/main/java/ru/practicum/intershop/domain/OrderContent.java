package ru.practicum.intershop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "shop", name = "order_content")
public class OrderContent {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "product_id")
    private Long productId;

    @Column(value = "order_id")
    private Long orderId;

    @Column(value = "quantity")
    private Integer quantity;

    @Column(value = "product_price_from_order")
    private Double productPriceFromOrder;

    @Transient
    private Mono<Product> product;

    @Transient
    private Mono<Order> order;

}
