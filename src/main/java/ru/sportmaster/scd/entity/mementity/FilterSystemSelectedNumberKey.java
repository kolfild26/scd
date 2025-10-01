package ru.sportmaster.scd.entity.mementity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FilterSystemSelectedNumberKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -458146392388046213L;

    @Column(name = "FORM_UUID", nullable = false)
    private String formUUID;
    @Column(name = "TABLE_NAME", nullable = false)
    private String tableName;
    @Column(name = "SEL_VALUE", nullable = false)
    private Long selValue;
}
