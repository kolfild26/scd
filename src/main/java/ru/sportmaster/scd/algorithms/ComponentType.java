package ru.sportmaster.scd.algorithms;

public enum ComponentType {
    ALGORITHM("algorithm"),
    ALGORITHM_STAGE("algorithm stage"),
    ALGORITHM_STEP("algorithm step");
    private final String componentDescribe;

    ComponentType(String definitionDescribe) {
        this.componentDescribe = definitionDescribe;
    }

    public String getComponentDescribe() {
        return componentDescribe;
    }
}
