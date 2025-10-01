package ru.sportmaster.scd.utils;

import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtil {
    @SafeVarargs
    public static <T> T[] concat(T[] first, T... second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String join(String... items) {
        return Stream.of(items).filter(i -> i != null && !i.isBlank()).collect(Collectors.joining("."));
    }

    @SafeVarargs
    public static <T> String join(T... items) {
        return Stream.of(items).filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining("."));
    }

    public static <T> void merge(List<T> source, @NotNull List<T> items) {
        items.forEach(source::remove);
        source.addAll(items);
    }

    public static <T> void changeListByPredicate(List<T> list, Predicate<T> predicate, Consumer<T> update) {
        if (nonNull(list) && !list.isEmpty()) {
            list.stream().filter(predicate).forEach(update);
        }
    }
}
