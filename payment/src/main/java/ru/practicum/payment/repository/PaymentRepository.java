package ru.practicum.payment.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.practicum.payment.domain.Payment;

public interface PaymentRepository extends R2dbcRepository<Payment, Long> {
}
