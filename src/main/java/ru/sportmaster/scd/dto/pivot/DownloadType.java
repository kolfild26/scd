package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Выбор формата файла сводной")
public enum DownloadType {
    EXCEL,
    PDF
}
