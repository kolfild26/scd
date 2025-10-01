package ru.sportmaster.scd.ui.view.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIViewMemSelection {
    @Schema(
        description = "Название view (сущности) из которой необходимо применить фильтрацию",
        name = "view",
        example = "LocationDepartmentME"
    )
    private String view;
    @Schema(
        description = "Поле в view к которому необходимо применить фильтр. Идентификатор магазина/товара.",
        name = "field",
        example = "shop.id"
    )
    private String field;
    @Schema(
        description = "Идентификатор view (сущности) из которой необходимо применить фильтрацию. "
            + "Возвращается из формы выбора товаров/магазинов",
        name = "formUuid",
        example = "xxxyyy"
    )
    private String formUuid;
}
