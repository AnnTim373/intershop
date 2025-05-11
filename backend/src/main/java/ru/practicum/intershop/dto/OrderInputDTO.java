package ru.practicum.intershop.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderInputDTO {

    private List<Item> items;

    @Data
    public static class Item {

        private Long productId;

        private Integer quantity;

    }

}
