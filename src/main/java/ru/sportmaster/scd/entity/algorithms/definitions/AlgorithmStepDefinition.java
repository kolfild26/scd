package ru.sportmaster.scd.entity.algorithms.definitions;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import ru.sportmaster.scd.algorithms.IAlgComponentDefine;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.persistence.MapJsonConverter;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_ALG_STEP_DEF", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Audited
public class AlgorithmStepDefinition implements IEntity<Long>, IAlgComponentDefine, Serializable {
    @Serial
    private static final long serialVersionUID = 4767496710349935173L;

    @Id
    @GenericGenerator(
        name = "algorithmStepDefinitionSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SCD_ALG_STEP_DEF"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "algorithmStepDefinitionSequence")
    @Column(name = "ID_ALG_STEP_DEF", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_ALG_STAGE_DEF", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private AlgorithmStageDefinition algorithmStageDefinition;
    @Column(name = "STEP_ORDER", precision = 5, nullable = false)
    private Integer order;
    @Column(name = "NAME", length = 256, nullable = false)
    private String name;
    @Column(name = "DESC_ALG_STEP", length = 2000)
    private String description;
    @Column(name = "DEF_PARAMS")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> defaultParams;
    @Column(name = "EXECUTOR_NAME", length = 256, nullable = false)
    private String executorName;
    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;

    @JsonIgnore
    public boolean nonDeleted() {
        return isNull(isDeleted) || !isDeleted;
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(id);
        var objectMapper = new ObjectMapper();
        out.writeObject(objectMapper.writeValueAsString(algorithmStageDefinition));
        out.writeObject(order);
        out.writeObject(name);
        out.writeObject(description);
        out.writeObject(objectMapper.writeValueAsString(defaultParams));
        out.writeObject(executorName);
        out.writeObject(isDeleted);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = (Long) in.readObject();
        var objectMapper = new ObjectMapper();
        algorithmStageDefinition =
            objectMapper.readValue(
                (String) in.readObject(),
                new TypeReference<>() {
                }
            );
        order = (Integer) in.readObject();
        name = (String) in.readObject();
        description = (String) in.readObject();
        defaultParams =
            objectMapper.readValue(
                (String) in.readObject(),
                new TypeReference<>() {
                }
            );
        executorName = (String) in.readObject();
        isDeleted = (Boolean) in.readObject();
    }
}
