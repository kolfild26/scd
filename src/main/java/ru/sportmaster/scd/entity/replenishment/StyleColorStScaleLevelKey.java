package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class StyleColorStScaleLevelKey implements Serializable {
    private static final long serialVersionUID = -7885447906651255677L;

    @Column(name = "ID_SC_LEVEL")
    private Long levelId;

    @Column(name = "ID_SC_ST_LEVEL")
    private Long stLevelId;
}
