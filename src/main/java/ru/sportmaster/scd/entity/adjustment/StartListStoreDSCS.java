package ru.sportmaster.scd.entity.adjustment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocRowEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CalendarEditorType;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewFieldRule;
import ru.sportmaster.scd.ui.view.type.DataFetchType;
import ru.sportmaster.scd.utils.PersistenceUtils;

@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocRowEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_V_COLL_DSCS_SHOP_MIN_UPA_INEX_DAT_STRTL", schema = "SCD")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class StartListStoreDSCS extends ListStoreDSCS implements IEntity<Long> {
    @Id
    @Column(name = "ID_COLL_DSCS_SHOP_MIN_UPA_INEX_DAT_STRTL")
    private Long id;
    @Column(name = "INTAKE_DAT")
    @ViewField(order = 10, rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate intakeDate;
    @Column(name = "EXIT_DAT")
    @ViewField(order = 11, rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate exitDate;
    @Column(name = "MIN_UPA")
    @ViewField(order = 12)
    private Integer minUpa;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        return PersistenceUtils.equals(this, o);
    }

    @Override
    public int hashCode() {
        return PersistenceUtils.hashCode(this);
    }
}
