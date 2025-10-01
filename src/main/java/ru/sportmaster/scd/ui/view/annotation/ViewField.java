package ru.sportmaster.scd.ui.view.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewField {
    int order() default Integer.MAX_VALUE;

    boolean editable() default true;

    boolean required() default true;

    ViewFieldRule[] rules() default {};

    ViewRenderer[] renderer() default {};
}
