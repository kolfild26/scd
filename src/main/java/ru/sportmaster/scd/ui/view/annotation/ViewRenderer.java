package ru.sportmaster.scd.ui.view.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewRenderer {
    String srcField() default "";

    Class<?>[] srcType() default {};

    String updField() default "";

    Class<?>[] updType() default {};
}
