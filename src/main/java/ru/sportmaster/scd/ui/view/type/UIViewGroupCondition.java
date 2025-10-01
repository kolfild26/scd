package ru.sportmaster.scd.ui.view.type;

import static java.util.Optional.ofNullable;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UIViewGroupCondition implements ICondition {
    private Boolean isSearch;
    private Join join;
    private List<ICondition> conditions;

    @Override
    public boolean isGroup() {
        return true;
    }

    public Boolean isSearch() {
        return ofNullable(isSearch).orElse(false);
    }
}
