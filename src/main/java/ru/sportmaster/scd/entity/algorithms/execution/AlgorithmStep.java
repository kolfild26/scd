package ru.sportmaster.scd.entity.algorithms.execution;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ru.sportmaster.scd.algorithms.IAlgComponent;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.persistence.MapJsonConverter;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_ALG_STEP", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlgorithmStep implements IEntity<Long>, IAlgComponent {
    @Id
    @GenericGenerator(
        name = "algorithmStepSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SCD_ALG_STEP"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "algorithmStepSequence")
    @Column(name = "ID_ALG_STEP", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_ALG_STAGE", nullable = false)
    @ToString.Exclude
    private AlgorithmStage algorithmStage;
    @ManyToOne
    @JoinColumn(name = "ID_ALG_STEP_DEF", nullable = false)
    @ToString.Exclude
    private AlgorithmStepDefinition algorithmStepDefinition;
    @Column(name = "VER_ALG_STEP_DEF", nullable = false)
    private Long defStepVersion;
    @Column(name = "PARAMS")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> params;
    @Column(name = "RESULT")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> result;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Override
    public AlgorithmStepDefinition getDefine() {
        return algorithmStepDefinition;
    }

    @Override
    public LocalDateTime getCancelTime() {
        return null;
    }

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
