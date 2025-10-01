package ru.sportmaster.scd.struct.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация описывающая структуру оракла.
 * Свойство value - имя структуры оракл.
 * Свойство arrayName - имя структуры оракл, описывающей массив данных структур. Если значение не указано, считается,
 *                      что такой массив имеет имя, совпадающее с именем структуры с добавлением суффикса _LIST.
 */
@Documented
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StructType {
    String value();

    String arrayName() default "";
}
