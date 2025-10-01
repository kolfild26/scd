package ru.sportmaster.scd.entity.algorithms.execution;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.persistence.MapJsonConverter;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_ALG_STAGE", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlgorithmStage implements IEntity<Long>, IAlgComponent {
    @Id
    @GenericGenerator(
        name = "algorithmStageSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SCD_ALG_STAGE"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "algorithmStageSequence")
    @Column(name = "ID_ALG_STAGE", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_ALGORITHM", nullable = false)
    @ToString.Exclude
    private Algorithm algorithm;
    @ManyToOne
    @JoinColumn(name = "ID_ALG_STAGE_DEF", nullable = false)
    @ToString.Exclude
    private AlgorithmStageDefinition algorithmStageDefinition;
    @Column(name = "VER_ALG_STAGE_DEF", nullable = false)
    private Long defStageVersion;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Column(name = "END_TIME")
    private LocalDateTime endTime;
    @Column(name = "PARAMS")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> params;
    @Column(name = "RESULT")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> result;

    @OneToMany(mappedBy = "algorithmStage")
    @ToString.Exclude
    private List<AlgorithmStep> algorithmSteps;

    @Override
    public AlgorithmStageDefinition getDefine() {
        return algorithmStageDefinition;
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
