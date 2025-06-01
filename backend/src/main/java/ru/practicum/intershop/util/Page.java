package ru.practicum.intershop.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {

    private List<T> content;

    private Pageable pageable;

    private Long totalElements;

}
