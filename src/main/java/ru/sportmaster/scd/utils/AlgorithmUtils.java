package ru.sportmaster.scd.utils;

import static java.util.Objects.isNull;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * Служебный класс, предоставляющий общий функционал работы с алгоритмами.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlgorithmUtils {
    /**
     * Начальный порядковый номер списков.
     */
    public static final int START_ORDER = 1;

    /**
     * Добавление в таблицу параметров, параметров по умолчанию, если еще не заданы.
     *
     * @param params    - таблица параметров
     * @param defParams - таблица параметров по умолчанию
     */
    public static void addParamsValues(@NonNull Map<String, Object> params,
                                       Map<String, Object> defParams) {
        if (isNull(defParams)) {
            return;
        }
        defParams.forEach((key, value) -> params.computeIfAbsent(key, k -> value));
    }
}
