package ru.sportmaster.scd.exceptions;

import ru.sportmaster.scd.algorithms.DefinitionType;

public class UnknownDefinitionException extends RuntimeException {
    final Long definitionId;
    final DefinitionType definitionType;

    public UnknownDefinitionException(Long definitionId,
                                      DefinitionType definitionType) {
        this.definitionId = definitionId;
        this.definitionType = definitionType;
    }

    @Override
    public String getMessage() {
        return String.format("Unknown %s with id %s", definitionType.getDefinitionDescribe(), definitionId);
    }
}
