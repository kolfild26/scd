package ru.sportmaster.scd.algorithms;

import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;

public enum DefinitionType {
    ALGORITHM_DEFINITION("algorithm definition"),
    ALGORITHM_STAGE_DEFINITION("algorithm stage definition"),
    ALGORITHM_STEP_DEFINITION("algorithm step definition");
    private final String definitionDescribe;

    DefinitionType(String definitionDescribe) {
        this.definitionDescribe = definitionDescribe;
    }

    public String getDefinitionDescribe() {
        return definitionDescribe;
    }

    public static DefinitionType getDefinitionType(IAlgComponentDefine componentDefine) {
        if (componentDefine instanceof AlgorithmDefinition) {
            return ALGORITHM_DEFINITION;
        }
        if (componentDefine instanceof AlgorithmStageDefinition) {
            return ALGORITHM_STAGE_DEFINITION;
        }
        if (componentDefine instanceof AlgorithmStepDefinition) {
            return ALGORITHM_STEP_DEFINITION;
        }
        throw new IllegalArgumentException(
            String.format("Unknown type componentDefine %s", componentDefine.toString()));
    }
}
