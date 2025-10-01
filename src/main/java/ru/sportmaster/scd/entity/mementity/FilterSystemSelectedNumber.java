package ru.sportmaster.scd.entity.mementity;

import static java.util.Objects.isNull;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.utils.PersistenceUtils;

@Entity
@Table(name = "ASCD_FS_SEL_NUM", schema = "SCD_API")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilterSystemSelectedNumber implements IEntity<FilterSystemSelectedNumberKey> {
    @Builder.Default
    @EmbeddedId
    private FilterSystemSelectedNumberKey id = new FilterSystemSelectedNumberKey();

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        return PersistenceUtils.equals(this, o);
    }

    @Override
    public int hashCode() {
        return isNull(getId()) ? 0 : Objects.hashCode(getId());
    }
}
