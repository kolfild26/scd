package ru.sportmaster.scd.entity.algorithms;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;
import ru.sportmaster.scd.persistence.AlgPartitionTypeAttributeConverter;
import ru.sportmaster.scd.task.AlgorithmTaskState;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_QUEUE_TASK_STORE", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QueueTaskStore implements IEntity<Long> {
    @Id
    @Column(name = "ID_ALGORITHM", precision = 38, nullable = false, updatable = false)
    private Long algorithmId;

    @Column(name = "PARTITION_TYPE")
    @Convert(converter = AlgPartitionTypeAttributeConverter.class)
    private AlgPartitionType partitionType;
    @Column(name = "ID_PARTITION")
    private Long partitionId;
    @Column(name = "ID_GROUP")
    private Long groupAlgorithm;
    @Column(name = "ID_CALC")
    private Long calcId;
    @Column(name = "IS_STOP_ON_ERROR", precision = 1)
    private Boolean isStopOnError;

    @Builder.Default
    @Embedded
    private AlgorithmTaskState taskState = new AlgorithmTaskState();

    @Override
    public Long getId() {
        return algorithmId;
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
