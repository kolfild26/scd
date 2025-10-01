package ru.sportmaster.scd.entity.forecast;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CalendarDayTypeKey implements Serializable {
    private static final long serialVersionUID = 2774288568847555637L;

    @Column(name = "ID_DAY_TYPE")
    private String dayTypeId;

    @Column(name = "ID_BUSINESS_TMA")
    private Long businessId;
}
