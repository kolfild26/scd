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
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.persistence.MapJsonConverter;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "SCD_ALGORITHM", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Algorithm implements IEntity<Long>, IAlgComponent {
    @Id
    @GenericGenerator(
        name = "algorithmSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SCD_ALGORITHM"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "algorithmSequence")
    @Column(name = "ID_ALGORITHM", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_ALG_DEF", nullable = false)
    @ToString.Exclude
    private AlgorithmDefinition algorithmDefinition;
    @Column(name = "VER_ALG_DEF", nullable = false)
    private Long defVersion;
    @Column(name = "ID_CALC")
    private Long calc;
    @Column(name = "PARAMS")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> params;
    @Column(name = "RESULT")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> result;
    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createTime;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Column(name = "END_TIME")
    private LocalDateTime endTime;
    @Column(name = "CANCEL_TIME")
    private LocalDateTime cancelTime;
    @Column(name = "USER_ID", precision = 38, nullable = false)
    private Long user;
    @Column(name = "OS_USER", length = 256)
    private String osUser;
    @Column(name = "HOST", length = 256)
    private String host;
    @Column(name = "IP_ADDRESS", length = 256)
    private String ipAddress;
    @Column(name = "MODULE", length = 256)
    private String module;

    @OneToMany(mappedBy = "algorithm")
    @ToString.Exclude
    private List<AlgorithmStage> algorithmStages;

    @Override
    public AlgorithmDefinition getDefine() {
        return algorithmDefinition;
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
