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

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "shop", name = "order")
public class Order {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "order_date_time")
    private LocalDateTime orderDateTime;

    @Transient
    private Mono<List<OrderContent>> contents;

}
