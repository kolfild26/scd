package ru.sportmaster.scd.dto.view;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = """
    `SAVE` - Сохранение выбора при закрытии окна,
    `SAVE_WBD` - Сохранение выбора при закрытии окна, иуз сохранения результата в БД,
    `CLEAR` - Очистка выбора при закрытии окна,
    `RESTORE` - Востановление выбора при открытии окна,
    `SELECT_ALL` - Выбрать все,
    `UNSELECT_ALL` - Снять выбор,
    `SELECT` - Выбрать указанные,
    `UNSELECT` - Снять выбор указанных
    """)
public enum SelectionCommand {
    SAVE,
    SAVE_WBD,
    CLEAR,
    RESTORE,
    SELECT_ALL,
    UNSELECT_ALL,
    SELECT_SEVERAL,
    UNSELECT_SEVERAL,
    SELECT,
    UNSELECT
}
