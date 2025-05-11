package ru.practicum.intershop.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.intershop.domain.Order;
import ru.practicum.intershop.domain.OrderContent;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.OrderInputDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;
import ru.practicum.intershop.mapper.OrderMapper;
import ru.practicum.intershop.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final ProductService productService;

    @Override
    public Order fromDTO(OrderInputDTO orderInputDTO) {
        List<OrderContent> contentList = getOrderContentFromDTO(orderInputDTO.getItems());
        Order order = Order.builder()
                .orderDateTime(LocalDateTime.now())
                .contents(contentList)
                .totalPrice(getTotalPrice(contentList))
                .build();
        contentList.forEach(content -> content.setOrder(order));
        return order;
    }

    private List<OrderContent> getOrderContentFromDTO(List<OrderInputDTO.Item> items) {
        if (items == null || items.isEmpty()) throw new IntershopException("Нельзя создать пустой заказ");
        return items.stream().map(item -> {
            Product product = productService.findById(item.getProductId())
                    .orElseThrow(() -> new IntershopException("Товар не найден"));
            return OrderContent.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .productPriceFromOrder(product.getPrice())
                    .build();
        }).collect(Collectors.toList());
    }

    private Double getTotalPrice(List<OrderContent> contentList) {
        return contentList.stream()
                .map(content -> content.getProduct().getPrice() * content.getQuantity())
                .reduce(0.0, Double::sum);
    }

}
