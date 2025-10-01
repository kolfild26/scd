package ru.sportmaster.scd.exceptions;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.algorithms.DefinitionType;
import ru.sportmaster.scd.algorithms.IAlgComponentDefine;

public class DeletedDefinitionException extends UnknownDefinitionException {
    private final String definitionDescription;

    public DeletedDefinitionException(@NonNull IAlgComponentDefine componentDefine) {
        super(componentDefine.getId(), DefinitionType.getDefinitionType(componentDefine));
        this.definitionDescription = componentDefine.toString();
    }

    @Override
    public String getMessage() {
        return String.format(
            "%s%s with id %s is deleted: %s",
            definitionType.getDefinitionDescribe().substring(0, 1).toUpperCase(),
            definitionType.getDefinitionDescribe().substring(1),
            definitionId,
            definitionDescription);
    }
}
