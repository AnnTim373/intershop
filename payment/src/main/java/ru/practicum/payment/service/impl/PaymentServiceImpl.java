package ru.practicum.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.payment.domain.Payment;
import ru.practicum.payment.dto.PaymentInputDTO;
import ru.practicum.payment.dto.PaymentOutputDTO;
import ru.practicum.payment.repository.PaymentRepository;
import ru.practicum.payment.service.AccountService;
import ru.practicum.payment.service.OrderContentService;
import ru.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderContentService orderContentService;
    private final AccountService accountService;
    private final PaymentRepository paymentRepository;

    @Override
    public Mono<PaymentOutputDTO> processPayment(Mono<PaymentInputDTO> dtoMono) {
        return dtoMono.flatMap(dto ->
                accountService.findAccountByUserId(dto.getUserId()).flatMap(account -> {
                    Mono<BigDecimal> totalPriceMono = orderContentService.getOrderContents(dto.getOrderId())
                            .map(orderContent -> orderContent.getProductPriceFromOrder()
                                    .multiply(BigDecimal.valueOf(orderContent.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return totalPriceMono.flatMap(totalPrice -> {
                        BigDecimal currentBalance = account.getBalance();
                        if (totalPrice.compareTo(currentBalance) <= 0) {
                            account.setBalance(account.getBalance().subtract(totalPrice));
                            return saveSucceededPayment(dto, totalPrice, account.getId())
                                    .map(payment -> {
                                        accountService.save(account);
                                        return new PaymentOutputDTO().status(true);
                                    });
                        } else {
                            return saveFailedPayment(dto, totalPrice, account.getId())
                                    .thenReturn(new PaymentOutputDTO().status(false));
                        }
                    });

                }));
    }

    private Mono<Payment> saveSucceededPayment(PaymentInputDTO dto, BigDecimal totalPrice, Long accountId) {
        return paymentRepository.save(Payment.builder()
                .status(Payment.PaymentStatus.SUCCESS)
                .paymentTimestamp(LocalDateTime.now())
                .orderId(dto.getOrderId())
                .accountId(accountId)
                .totalAmount(totalPrice)
                .build());
    }

    private Mono<Payment> saveFailedPayment(PaymentInputDTO dto, BigDecimal totalPrice, Long accountId) {
        return paymentRepository.save(Payment.builder()
                .status(Payment.PaymentStatus.ERROR)
                .paymentTimestamp(LocalDateTime.now())
                .orderId(dto.getOrderId())
                .accountId(accountId)
                .totalAmount(totalPrice)
                .build());
    }

}
