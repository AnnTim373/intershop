package ru.practicum.intershop.util;

import java.lang.reflect.Field;
import java.util.List;

public class SortUtils {

    @SuppressWarnings("all")
    public static <T> void sortByField(List<T> list, String fieldName, String sortDirection) {
        if (list == null || list.isEmpty() || fieldName == null || sortDirection == null) return;

        list.sort((o1, o2) -> {
            try {
                Field field = o1.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                Object value1 = field.get(o1);
                Object value2 = field.get(o2);

                if (value1 == null && value2 == null) return 0;
                if (value1 == null) return sortDirection.equalsIgnoreCase("asc") ? -1 : 1;
                if (value2 == null) return sortDirection.equalsIgnoreCase("asc") ? 1 : -1;

                if (value1 instanceof Comparable && value2 instanceof Comparable) {
                    int result = ((Comparable) value1).compareTo(value2);
                    return sortDirection.equalsIgnoreCase("asc") ? result : -result;
                } else {
                    throw new IllegalArgumentException("Поле не реализует Comparable: " + fieldName);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Ошибка доступа к полю: " + fieldName, e);
            }
        });
    }
}
