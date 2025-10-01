package ru.sportmaster.scd.entity.algorithms.definitions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_ALG_DEF_REVINFO", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@RevisionEntity
public class AlgDefRevInfo implements IEntity<Long> {
    @Id
    @RevisionNumber
    @GenericGenerator(
        name = "algorithmStageDefinitionRevInfoSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SCD_ALG_DEF_REVINFO"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "algorithmStageDefinitionRevInfoSequence")
    @Column(name = "REV", nullable = false)
    private Long id;

    @RevisionTimestamp
    @Column(name = "REVTSTMP")
    private Long timestamp;

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
