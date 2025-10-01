package ru.sportmaster.scd.entity.dictionary;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.ui.view.annotation.View;

@Entity
@View
@Table(name = "SCD_MFP_CLND_WEEK_SM", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Week implements IEntity<Long> {
    @Id
    @Column(name = "ID_WEEK", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "WEEK")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate name;
    @Column(name = "WEEK_LABEL")
    private String label;
}
