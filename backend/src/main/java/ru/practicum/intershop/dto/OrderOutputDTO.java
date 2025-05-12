package ru.practicum.intershop.dto;

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
public class OrderOutputDTO {

    private Long id;

    private Double totalPrice;

    private LocalDateTime orderDate;

    private List<OrderContentOutputDTO> contents;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderContentOutputDTO {

        private String productName;

        private Integer quantity;

        private Double price;

    }

}
