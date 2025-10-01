package ru.sportmaster.scd.ui.view.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UiViewNode {
    private ObjectNode value;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UiViewNode> children;

    public static UiViewNode of(ObjectNode node) {
        return UiViewNode.builder().value(node).build();
    }
}
