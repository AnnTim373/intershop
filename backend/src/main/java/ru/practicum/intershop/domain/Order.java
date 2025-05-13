package ru.practicum.intershop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "shop", name = "order")
@SequenceGenerator(schema = "shop", sequenceName = "seq_order", name = "seq_order", allocationSize = 1)
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order")
    private Long id;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<OrderContent> contents;

}
