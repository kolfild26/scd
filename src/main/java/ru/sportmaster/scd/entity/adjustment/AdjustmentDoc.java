package ru.sportmaster.scd.entity.adjustment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.service.adjustment.AdjustmentDocJpaCallbackListener;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_CORRECTION_LIST", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners({ AdjustmentDocJpaCallbackListener.class })
public class AdjustmentDoc implements IEntity<Long> {
    @Id
    @ViewField(order = 0, editable = false)
    @Column(name = "ID_CORRECTION_LIST")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_TYPE")
    @View(flat = true)
    @ViewField(order = 1, editable = false)
    private AdjustmentDocType type;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 2, editable = false)
    private Business business;
    @ViewField(order = 3)
    @Column(name = "DESCRIPTION", length = 1000)
    private String comment;
    @ManyToOne
    @JoinColumn(name = "ID_MAP_USER_ON_SYSTEM")
    @View(flat = true)
    @ViewField(order = 4, editable = false)
    private AdjustmentDocUserOnSystem userOnSystem;
    @Column(name = "CREATE_DAT")
    @ViewField(editable = false, order = 5)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date date;
    @Column(name = "APPROVE_DAT")
    @ViewField(editable = false, order = 6)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date approveDate;
    @Column(name = "CANCELLATION_DAT")
    @ViewField(editable = false, order = 7)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date cancellationDate;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_STATUS")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 8)
    private AdjustmentDocStatus status;
    @Transient
    private Boolean isEmpty;
}
