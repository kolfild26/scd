package ru.sportmaster.scd.ui.view.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@Documented
@Target({ TYPE })
@Retention(RUNTIME)
public @interface CustomView {
    String name() default "";

    boolean recoverable() default false;

    Class<? extends UiViewResolver> resolver();

    Class<? extends UiViewEditor>[] editor() default {};

    DataFetchType fetchType() default DataFetchType.FULL;
}
