package ru.sportmaster.scd.entity.simple3dplan;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode
public class InitialDistrSourceKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 7623872771106313813L;

    @Column(name = "ID_COLLECTION")
    private Long collectionId;

    @Column(name = "ID_BUSINESS_TMA")
    private Long businessId;
}
