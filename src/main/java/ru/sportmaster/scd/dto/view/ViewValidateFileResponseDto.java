package ru.sportmaster.scd.dto.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.sportmaster.scd.dto.Status;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ViewValidateFileResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2794356128706635577L;

    @Schema(
        description = "Уникальный идентификатор загруженного документа",
        name = "uuid"
    )
    private String uuid;
    @Schema(
        description = "Название view",
        name = "view"
    )
    private String view;
    @Schema(
        description = "Статус обработки документа",
        name = "status",
        example = "PROCESS"
    )
    private Status status;
    @Schema(
        description = "Прогресс обработки документа",
        name = "progress"
    )
    @Builder.Default
    private Float progress = 0f;
    @Schema(
        description = "Наличие блокирующих ошибок",
        name = "hasBlockedErrors"
    )
    @Builder.Default
    private Boolean hasBlockedErrors = false;
    @Schema(
        description = "Описание блокирующей ошибки",
        name = "errorText"
    )
    private String blockedErrorText;
    @Schema(
        description = "Количество ошибок в файле",
        name = "errorsCount"
    )
    @Builder.Default
    private Integer errorsCount = 0;
    @Schema(
        description = "Список ошибок валидации",
        name = "errors",
        example = "[{\"rowIndex\":1,\"field\":\"intakeDate\","
            + "\"key\":\"FIELD_NOT_FOUND\",\"label\":\"Поле не найдено!\"}]"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AdjustmentValidationErrorDto> errors;
    @Schema(
        description = "Содержимое документа в формате json",
        name = "data"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ObjectNode> data;
    @JsonIgnore
    private List<UiViewCrudRequest> requests;

    @Schema(
        description = "Признак валидности файла. Доступен ли для загрузки",
        name = "valid"
    )
    public boolean isValid() {
        return !hasBlockedErrors && errorsCount == 0;
    }

    public ViewValidateFileResponseDto fileError() {
        return ViewValidateFileResponseDto.builder()
            .status(Status.ERROR)
            .errorsCount(errorsCount)
            .progress(100f)
            .build();
    }

    public static ViewValidateFileResponseDto start() {
        return ViewValidateFileResponseDto.builder()
            .status(Status.QUEUE)
            .build();
    }

    public static ViewValidateFileResponseDto progress(float progress, int errorsCount) {
        return ViewValidateFileResponseDto.builder()
            .status(Status.PROCESS)
            .progress(progress)
            .errorsCount(errorsCount)
            .build();
    }

    public static ViewValidateFileResponseDto blockedError(String text) {
        return ViewValidateFileResponseDto.builder()
            .status(Status.ERROR)
            .hasBlockedErrors(true)
            .blockedErrorText(text)
            .progress(100f)
            .build();
    }

    public static ViewValidateFileResponseDto done(String view, List<UiViewCrudRequest> requests) {
        return ViewValidateFileResponseDto.builder()
            .view(view)
            .status(Status.DONE)
            .requests(requests)
            .progress(100f)
            .build();
    }
}
