package ru.sportmaster.scd.entity.adjustment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "SCD_DC_REF_CORRECTION_TYPE", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({ "type" })
public class AdjustmentDocType implements IEntity<Long> {
    @Id
    @Column(name = "ID_CORRECTION_TYPE")
    private Long id;
    @ViewField
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "FULL_DESCRIPTION")
    private String fullDescription;
    @ViewField(editable = false)
    @Column(name = "LEVEL_TYPE")
    private String level;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_GROUP")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    private AdjustmentDocTypeGroup group;
    @Transient
    private AdjustmentType type;

    @PostLoad
    public void fillTransparent() {
        this.type = AdjustmentType.findById(id);
    }
}
