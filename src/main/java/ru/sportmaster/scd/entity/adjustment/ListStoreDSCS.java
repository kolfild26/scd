package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@MappedSuperclass
//Lombok
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListStoreDSCS {
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    protected Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_DEV_STYLECOLOR_SIZE")
    @View(flat = true)
    protected DevColorSize size;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    protected LocationDepartment shop;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    protected AdjustmentDoc document;
}
