package ru.sportmaster.scd.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.ui.view.annotation.View;

@View(name = "User")
@Entity
@Table(name = "SCD_USER", schema = "SCD")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class UserShort implements IEntity<Long> {
    @Id
    @Column(name = "USER_ID", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "LOGIN", nullable = false)
    private String login;
    @Column(name = "LABEL")
    private String label;
}
