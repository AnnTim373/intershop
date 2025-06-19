package ru.practicum.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "shop", name = "order_content")
public class OrderContent {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "order_id")
    private Long orderId;

    @Column(value = "quantity")
    private Integer quantity;

    @Column(value = "product_price_from_order")
    private BigDecimal productPriceFromOrder;

}
