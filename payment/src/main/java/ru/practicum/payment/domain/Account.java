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
@Table(schema = "shop", name = "account")
public class Account {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "balance")
    private BigDecimal balance;

}
