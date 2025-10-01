package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategy;
import ru.sportmaster.scd.ui.view.annotation.CalendarEditorType;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewFieldRule;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@MappedSuperclass
//Lombok
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplenishmentStrategySC {
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 1)
    protected Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_STRAT_MAN_BEGIN")
    @ViewField(
        order = 2,
        required = false,
        renderer = @ViewRenderer(srcField = "startDay", srcType = LocalDate.class, updType = LocalDate.class),
        rules = @ViewFieldRule(dayOfWeek = 1, calendarType = CalendarEditorType.DAY)
    )
    protected Day startDay;
    @ManyToOne
    @JoinColumn(name = "ID_STRAT_MAN_END")
    @ViewField(
        order = 3,
        required = false,
        renderer = @ViewRenderer(srcField = "endDay", srcType = LocalDate.class, updType = LocalDate.class),
        rules = @ViewFieldRule(dayOfWeek = 7, calendarType = CalendarEditorType.DAY)
    )
    protected Day endDay;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 4)
    protected LocationDepartment shop;
    @ManyToOne
    @JoinColumn(name = "ID_REPL_STRATEGY")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 6)
    protected ReplenishmentStrategy value;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    protected AdjustmentDoc document;
}
