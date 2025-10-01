package ru.sportmaster.scd.ui.view.annotation;

public @interface ViewFieldRule {
    CalendarEditorType calendarType() default CalendarEditorType.WEEK;
    int dayOfWeek() default 0;
}
