package ru.sportmaster.scd.mementity;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewMemSelection;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MemEntityFetchParams {
    @Builder.Default
    private Boolean full = false;
    private List<ICondition> conditions;
    private List<UIViewMemSelection> memSelections;
    private Map<String, Sort.Direction> sort;

    public static MemEntityFetchParams createFromRequest(@NonNull UiViewFetchRequest request) {
        return
            MemEntityFetchParams.builder()
                .full(request.isFull())
                .conditions(request.getConditions())
                .memSelections(request.getMemSelections())
                .sort(request.getSort())
                .build();
    }
}
