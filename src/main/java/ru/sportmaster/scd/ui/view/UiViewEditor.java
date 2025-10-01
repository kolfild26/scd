package ru.sportmaster.scd.ui.view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

public interface UiViewEditor {
    ObjectNode change(UiViewCrudRequest request);
}
