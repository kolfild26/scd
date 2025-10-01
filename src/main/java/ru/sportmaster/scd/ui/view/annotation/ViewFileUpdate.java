package ru.sportmaster.scd.ui.view.annotation;

public @interface ViewFileUpdate {
    boolean creatable() default false;
    boolean updatable() default false;
}
