package ru.sportmaster.scd.task;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AlgorithmTaskState implements Serializable {
    @Serial
    private static final long serialVersionUID = -8196927005307117056L;
    @Column(name = "STATUS", length = 30)
    @Enumerated(EnumType.STRING)
    private AlgorithmTaskStatus status;
    @Column(name = "CREATED")
    private LocalDateTime created;
    @Column(name = "STARTED")
    private LocalDateTime started;
    @Column(name = "CANCELED")
    private LocalDateTime canceled;
    @Column(name = "ORDER_TASK")
    private Long order;
    @Builder.Default
    @Column(name = "NORMAL_DONE", precision = 1)
    private boolean isNormalDone = true;
}
