package ru.sportmaster.scd.ui.view.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@Documented
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface View {
    String name() default "";
    boolean flat() default false;
    boolean recoverable() default false;
    DataFetchType fetchType() default DataFetchType.INFINITY;
    ViewFileUpdate[] fileUpdate() default { @ViewFileUpdate };
}
