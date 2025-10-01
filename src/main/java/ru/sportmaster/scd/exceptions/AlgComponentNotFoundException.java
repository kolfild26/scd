package ru.sportmaster.scd.exceptions;

import ru.sportmaster.scd.algorithms.ComponentType;

public class AlgComponentNotFoundException extends RuntimeException {
    final Long componentId;
    final ComponentType componentType;

    public AlgComponentNotFoundException(Long componentId,
                                         ComponentType componentType) {
        this.componentId = componentId;
        this.componentType = componentType;
    }

    @Override
    public String getMessage() {
        return String.format(
            "%s%s with id %s not found.",
            componentType.getComponentDescribe().substring(0,1).toUpperCase(),
            componentType.getComponentDescribe().substring(1),
            componentId);
    }
}
