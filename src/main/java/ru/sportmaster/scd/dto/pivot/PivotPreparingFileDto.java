package ru.sportmaster.scd.dto.pivot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.dto.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Получение статуса формирования файла сводной при скачивании")
public class PivotPreparingFileDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -4580538919532056309L;

    @Schema(
        name = "status",
        description = "Статус формируемого файла"
    )
    private Status status;
    @Schema(
        name = "progress",
        description = "Прогресс формируемого файла"
    )
    private float progress;
    @JsonIgnore
    private RuntimeException exception;
    @JsonIgnore
    private byte[] data;

    public static PivotPreparingFileDto start() {
        return PivotPreparingFileDto.builder()
            .status(Status.QUEUE)
            .progress(0)
            .build();
    }

    public static PivotPreparingFileDto process(float progress) {
        return PivotPreparingFileDto.builder()
            .status(Status.PROCESS)
            .progress(progress)
            .build();
    }

    public static PivotPreparingFileDto error(RuntimeException exception) {
        return PivotPreparingFileDto.builder()
            .status(Status.ERROR)
            .progress(0)
            .exception(exception)
            .build();
    }

    public static PivotPreparingFileDto done(byte[] data) {
        return PivotPreparingFileDto.builder()
            .status(Status.DONE)
            .progress(100)
            .data(data)
            .build();
    }
}
