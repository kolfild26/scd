package ru.sportmaster.scd.entity.dictionary;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode
public class RDHierarchyGeneralKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -5081213563055680962L;

    @Column(name = "ID_NODE", precision = 14, nullable = false, updatable = false)
    private Long id;

    @Column(name = "LEVEL_NUM", precision = 14, nullable = false, updatable = false)
    private String level;
}
