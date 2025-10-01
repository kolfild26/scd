package ru.sportmaster.scd.algorithms.information;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import ru.sportmaster.scd.algorithms.IAlgComponent;
import ru.sportmaster.scd.algorithms.IAlgComponentDefine;

public record AlgorithmInfoParam(
    Long id,
    Long definitionId,
    String name,
    String description,
    Integer order,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime cancelTime,
    Map<String, Object> result
) {
    public static AlgorithmInfoParam create(IAlgComponentDefine componentDefine,
                                            IAlgComponent component) {
        return new AlgorithmInfoParam(
            Optional.ofNullable(component).map(IAlgComponent::getId).orElse(null),
            Optional.ofNullable(componentDefine)
                .map(IAlgComponentDefine::getId)
                .orElse(
                    Optional.ofNullable(component)
                        .map(IAlgComponent::getDefine)
                        .map(IAlgComponentDefine::getId)
                        .orElse(null)
                ),
            Optional.ofNullable(componentDefine)
                .map(IAlgComponentDefine::getName)
                .orElse("UNDEFINED"),
            Optional.ofNullable(componentDefine)
                .map(IAlgComponentDefine::getDescription)
                .orElse("UNDEFINED"),
            Optional.ofNullable(componentDefine)
                .map(IAlgComponentDefine::getOrder)
                .orElse(Integer.MAX_VALUE),
            Optional.ofNullable(component).map(IAlgComponent::getStartTime).orElse(null),
            Optional.ofNullable(component).map(IAlgComponent::getEndTime).orElse(null),
            Optional.ofNullable(component).map(IAlgComponent::getCancelTime).orElse(null),
            Optional.ofNullable(component).map(IAlgComponent::getResult).orElse(null)
        );
    }
}
