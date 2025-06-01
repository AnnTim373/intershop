package ru.practicum.intershop.util;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor
public final class PaginationUtil {

    public static <T> HttpHeaders generatePaginationHttpHeaders(Page<T> page) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return headers;
    }

}
