package ru.sportmaster.scd.entity.returnlogistic;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import ru.sportmaster.scd.ui.view.annotation.View;

@View
@Entity
@Table(name = "SCD_RL_RETURN_TYPE_SETTING", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReturnTypeSetting {
    @Id
    @Column(name = "ID_RETURN_TYPE_SETTING")
    private Long id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;
}
