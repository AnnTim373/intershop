package ru.practicum.intershop.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Pageable {

    private Integer size;

    private Integer page;

    private String sort;

    public Sort getNormalizeSort() {
        if (sort == null || sort.isEmpty()) return new Sort("id", "desc");
        String[] sorts = sort.split(",");
        if (sorts.length != 2) return new Sort("id", "desc");
        return new Sort(sorts[0], sorts[1]);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sort {

        private String column;

        private String direction;

        @Override
        public String toString() {
            return column + " " + direction;
        }

    }

}
