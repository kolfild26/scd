package ru.sportmaster.scd.entity.mementity;

import static java.util.Objects.isNull;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = "ASCD_FS_LOG", schema = "SCD_API")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilterSystemLog implements IEntity<FilterSystemLogKey> {
    @Builder.Default
    @EmbeddedId
    private FilterSystemLogKey id = new FilterSystemLogKey();
    @Column(name = "TYPE_SELECTION", nullable = false, length = 1)
    private Byte typeSelection;
    @Column(name = "CREATED")
    private LocalDateTime created;
    @Column(name = "MODIFIED")
    private LocalDateTime modified;
    @Column(name = "CLOSED")
    private LocalDateTime closed;
    @Column(name = "USER_ID")
    private Long userId;

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
