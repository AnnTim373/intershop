package ru.practicum.intershop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OrderInputDTO {

    private Long accountId;

    private List<ContentDTO> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentDTO {

        private Long productId;

        private Integer quantity;

    }

}
