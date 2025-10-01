package ru.sportmaster.scd.dto.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.dto.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgSettingsStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1014075275589922587L;

    @Schema(
        description = "Тип блокировки",
        name = "type"
    )
    private LockType type;
    @Schema(
        description = "Время начала блокировки",
        name = "startTime"
    )
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime startTime;
    @Schema(
        description = "Прогнозируемое время окончания блокировки",
        name = "unlockTime"
    )
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime unlockTime;
    @Schema(
        description = "Пользователь создавший блокировку (если есть)",
        name = "user"
    )
    private UserDto user;
}
