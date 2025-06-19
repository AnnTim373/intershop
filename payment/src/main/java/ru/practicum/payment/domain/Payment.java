package ru.practicum.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "shop", name = "payment")
public class Payment {

    @Id
    private Long id;

    private BigDecimal totalAmount;

    private Long orderId;

    private Long accountId;

    private PaymentStatus status;

    private LocalDateTime paymentTimestamp;

    public enum PaymentStatus {
        SUCCESS, ERROR
    }

}
