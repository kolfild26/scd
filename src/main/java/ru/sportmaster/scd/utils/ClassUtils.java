package ru.sportmaster.scd.utils;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.utils.CollectionUtil.concat;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtils {

    public static boolean isAnnotated(Field field, Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation) || field.getType().isAnnotationPresent(annotation);
    }

    public static boolean isEntity(Class<?> clazz) {
        return clazz.isAnnotationPresent(Entity.class);
    }

    public static boolean isEnum(Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz);
    }

    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isCollectionEntity(Field field) {
        if (isCollection(field.getType())) {
            Class<?> genericType = getGenericFirstType(field);
            return genericType != null && isEntity(genericType);
        }
        return false;
    }

    public static <T extends Annotation> Optional<T> getAnnotation(Field field, Class<T> annotation) {
        return field.isAnnotationPresent(annotation) ? Optional.of(field.getAnnotation(annotation)) : Optional.empty();
    }

    public static <T extends Annotation> Optional<T> getAnnotation(Class<?> clazz, Class<T> annotation) {
        return clazz.isAnnotationPresent(annotation) ? Optional.of(clazz.getAnnotation(annotation)) : Optional.empty();
    }

    public static Stream<Field> getFieldsStream(Class<?> clazz) {
        Stream<Field> stream = stream(concat(clazz.getFields(), clazz.getDeclaredFields()));
        Class<?> superClazz = clazz.getSuperclass();
        if (nonNull(superClazz) && superClazz != Object.class) {
            stream = Stream.concat(stream, getFieldsStream(clazz.getSuperclass()));
        }
        return stream;
    }

    public static Class<?> getGenericFirstType(Field field) {
        return Optional.of((ParameterizedType) field.getGenericType())
            .map(i -> (Class<?>) i.getActualTypeArguments()[0])
            .orElse(null);
    }

    public static String getIdFieldName(Class<?> clazz) {
        return getFieldsStream(clazz)
            .filter(i -> i.isAnnotationPresent(Id.class) || i.isAnnotationPresent(EmbeddedId.class))
            .map(Field::getName).findFirst().orElse(null);
    }

    public static Field getFieldByName(Class<?> clazz, String name) {
        return getFieldsStream(clazz)
            .filter(i -> Objects.equals(i.getName(), name))
            .findFirst().orElse(null);
    }

    public static boolean hasEmbeddedId(Class<?> clazz) {
        return getFieldsStream(clazz).anyMatch(i -> i.isAnnotationPresent(EmbeddedId.class));
    }

    @SneakyThrows
    public static void setFieldValue(Class<?> clazz, Object target, String path, Object value) {
        var field = clazz.getDeclaredField(path);
        field.setAccessible(true);
        field.set(target, value);
    }

    @SneakyThrows
    public static <T> T newInstance(Class<T> clazz) {
        return clazz.getConstructor().newInstance();
    }
}
