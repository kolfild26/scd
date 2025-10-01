package ru.sportmaster.scd.service.view.io.preprocessor;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ViewFileRequestBuilderPreprocessor {
    Class<?> getTargetClass();

    void apply(String uuid, ObjectNode node);

    boolean validate(String uuid, ObjectNode node);

    boolean isEditable(String uuid, ObjectNode node);

    void clear(String uuid);
}
