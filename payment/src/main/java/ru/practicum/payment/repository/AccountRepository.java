package ru.practicum.payment.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.practicum.payment.domain.Account;

public interface AccountRepository extends R2dbcRepository<Account, Long> {
}
