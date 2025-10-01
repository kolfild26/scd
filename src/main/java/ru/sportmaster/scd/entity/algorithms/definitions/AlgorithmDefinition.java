package ru.sportmaster.scd.entity.algorithms.definitions;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.utils.AlgorithmUtils.START_ORDER;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import ru.sportmaster.scd.persistence.AlgPartitionTypeAttributeConverter;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.persistence.MapJsonConverter;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_ALGORITHM_DEF", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Audited
public class AlgorithmDefinition implements IEntity<Long>, IAlgComponentDefine, Serializable {
    @Serial
    private static final long serialVersionUID = -8616071804671440264L;

    @Id
    @GenericGenerator(
        name = "algorithmDefinitionSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SCD_ALGORITHM_DEF"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "algorithmDefinitionSequence")
    @Column(name = "ID_ALG_DEF", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "NAME", length = 256, nullable = false)
    private String name;
    @Column(name = "DESC_ALG", length = 2000)
    private String description;
    @Column(name = "OWNER", length = 256, nullable = false)
    private String owner;
    @Column(name = "IS_RELEVANCE", precision = 1)
    private Boolean isRelevance;
    @Column(name = "DEF_PARAMS")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> defaultParams;
    @Column(name = "PARTITION_TYPE")
    @Convert(converter = AlgPartitionTypeAttributeConverter.class)
    private AlgPartitionType partitionType;
    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "algorithmDefinition", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<AlgorithmStageDefinition> algorithmStageDefinitions;

    @JsonIgnore
    public boolean isDelete() {
        return Optional.ofNullable(isDeleted).orElse(false);
    }

    @JsonIgnore
    @Override
    public Integer getOrder() {
        return START_ORDER;
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
        out.writeObject(name);
        out.writeObject(description);
        out.writeObject(owner);
        out.writeObject(isRelevance);
        out.writeObject(new ObjectMapper().writeValueAsString(defaultParams));
        out.writeObject(partitionType.name());
        out.writeObject(isDeleted);
        if (nonNull(algorithmStageDefinitions)) {
            out.writeObject(algorithmStageDefinitions.size());
            for (var algorithmStageDefinition : algorithmStageDefinitions) {
                out.writeObject(algorithmStageDefinition);
            }
        } else {
            out.writeObject(0);
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = (Long) in.readObject();
        name = (String) in.readObject();
        description = (String) in.readObject();
        owner = (String) in.readObject();
        isRelevance = (Boolean) in.readObject();
        defaultParams =
            new ObjectMapper().readValue(
                (String) in.readObject(),
                new TypeReference<>() {
                }
            );
        partitionType = AlgPartitionType.valueOf((String) in.readObject());
        isDeleted = (Boolean) in.readObject();
        int size = (int) in.readObject();
        algorithmStageDefinitions = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            algorithmStageDefinitions.add((AlgorithmStageDefinition) in.readObject());
        }
    }
}
