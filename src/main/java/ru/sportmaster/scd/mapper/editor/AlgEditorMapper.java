package ru.sportmaster.scd.mapper.editor;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.editor.AlgEditorSchemaDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellDataDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellType;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition_;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition_;
import ru.sportmaster.scd.repository.algorithms.definitions.AlgorithmDefinitionRepository;

@Component
@RequiredArgsConstructor
public class AlgEditorMapper {
    private final ObjectMapper mapper;
    private final AlgorithmDefinitionRepository algorithmDefinitionRepository;

    public CellDataDto mapStage(AlgorithmStageDefinition stage) {
        return CellDataDto.builder()
            .cuid(UUID.randomUUID().toString())
            .type(CellType.STAGE)
            .node(mapper.valueToTree(stage))
            .build();
    }

    public CellDataDto mapStep(AlgorithmStepDefinition step) {
        return CellDataDto.builder()
            .cuid(UUID.randomUUID().toString())
            .type(CellType.STEP)
            .node(mapper.valueToTree(step))
            .build();
    }

    public List<CellDataDto> toDtos(AlgorithmDefinition definition) {
        if (definition.getAlgorithmStageDefinitions() == null) {
            return Collections.emptyList();
        }

        return definition.getAlgorithmStageDefinitions().stream().distinct()
            .filter(AlgorithmStageDefinition::nonDeleted)
            .sorted(Comparator.comparing(AlgorithmStageDefinition::getOrder))
            .map(stage -> {
                String stageUuid = UUID.randomUUID().toString();
                return CellDataDto.builder()
                    .cuid(stageUuid)
                    .type(CellType.STAGE)
                    .node(mapper.valueToTree(stage))
                    .children(convertSteps(stageUuid, stage))
                    .build();
            }).toList();
    }

    private List<CellDataDto> convertSteps(String parentUuid, AlgorithmStageDefinition stage) {
        if (stage.getAlgorithmStepDefinitions() == null) {
            return Collections.emptyList();
        }

        return stage.getAlgorithmStepDefinitions().stream().distinct()
            .filter(AlgorithmStepDefinition::nonDeleted)
            .sorted(Comparator.comparing(AlgorithmStepDefinition::getOrder))
            .map(step -> CellDataDto.builder()
                .cuid(UUID.randomUUID().toString())
                .parentCuid(parentUuid)
                .type(CellType.STEP)
                .node(mapper.valueToTree(step))
                .build()
            ).toList();
    }

    public AlgorithmDefinition toEntity(AlgEditorSchemaDto schema) {
        AlgorithmDefinition definition = getOrCreateAlgDefinition(schema.getId());
        definition.setName(schema.getName());
        definition.setDescription(schema.getDescription());
        definition.setDefaultParams(schema.getDefaultParams());

        List<CellDataDto> createAndUpdateChanges = schema.getCreateAndUpdateChanges();
        Map<String, AlgorithmStageDefinition> stageDefinitions = new HashMap<>();

        schema.getDeleteChanges().forEach(cell -> {
            if (CellType.STAGE == cell.getType()) {
                long deletedId = cell.getNode().get(AlgorithmStageDefinition_.ID).asLong();
                definition.getAlgorithmStageDefinitions().stream()
                    .filter(i -> i.getId() == deletedId).findFirst()
                    .ifPresent(stage -> stage.setIsDeleted(true));
            } else if (CellType.STEP == cell.getType()) {
                long deletedId = cell.getNode().get(AlgorithmStepDefinition_.ID).asLong();
                definition.getAlgorithmStageDefinitions().forEach(stage ->
                    stage.getAlgorithmStepDefinitions().stream()
                        .filter(i -> i.getId() == deletedId).findFirst()
                        .ifPresent(step -> step.setIsDeleted(true))
                );
            }
        });

        getCellDataByType(createAndUpdateChanges, CellType.STAGE).forEach(cell -> {
            if (definition.getAlgorithmStageDefinitions() == null) {
                definition.setAlgorithmStageDefinitions(new ArrayList<>());
            }

            AlgorithmStageDefinition stage = mapAlgStageDefinition(definition, cell);
            stage.setAlgorithmDefinition(definition);
            stageDefinitions.put(cell.getCuid(), stage);
        });

        getCellDataByType(createAndUpdateChanges, CellType.STEP).forEach(cell -> {
            AlgorithmStageDefinition parentStage = stageDefinitions.get(cell.getParentCuid());
            if (parentStage.getAlgorithmStepDefinitions() == null) {
                parentStage.setAlgorithmStepDefinitions(new ArrayList<>());
            }

            AlgorithmStepDefinition step = mapAlgStepDefinition(parentStage, cell);
            step.setAlgorithmStageDefinition(parentStage);
        });

        return definition;
    }

    private AlgorithmDefinition getOrCreateAlgDefinition(Long algDefId) {
        return ofNullable(algDefId).map(algorithmDefinitionRepository::findById).orElse(new AlgorithmDefinition());
    }

    private AlgorithmStageDefinition mapAlgStageDefinition(AlgorithmDefinition parent, CellDataDto cell) {
        JsonNode idNode = cell.getNode().get(AlgorithmStageDefinition_.ID);
        AlgorithmStageDefinition changed = mapper.convertValue(cell.getNode(), AlgorithmStageDefinition.class);
        if (idNode == null || idNode.isNull()) {
            parent.getAlgorithmStageDefinitions().add(changed);
            return changed;
        } else {
            return parent.getAlgorithmStageDefinitions().stream()
                .filter(i -> i.getId() == idNode.asLong()).findFirst()
                .map(definition -> {
                    definition.setName(changed.getName());
                    definition.setOrder(changed.getOrder());
                    definition.setDescription(changed.getDescription());
                    definition.setDefaultParams(changed.getDefaultParams());
                    definition.setIsDeleted(changed.getIsDeleted());
                    return definition;
                }).orElse(null);
        }
    }

    private AlgorithmStepDefinition mapAlgStepDefinition(AlgorithmStageDefinition parent, CellDataDto cell) {
        JsonNode idNode = cell.getNode().get(AlgorithmStepDefinition_.ID);
        AlgorithmStepDefinition changed = mapper.convertValue(cell.getNode(), AlgorithmStepDefinition.class);
        if (idNode == null || idNode.isNull()) {
            parent.getAlgorithmStepDefinitions().add(changed);
            return changed;
        } else {
            return parent.getAlgorithmStepDefinitions().stream()
                .filter(i -> i.getId() == idNode.asLong()).findFirst()
                .map(definition -> {
                    definition.setName(changed.getName());
                    definition.setOrder(changed.getOrder());
                    definition.setDescription(changed.getDescription());
                    definition.setDefaultParams(changed.getDefaultParams());
                    definition.setExecutorName(definition.getExecutorName());
                    definition.setIsDeleted(changed.getIsDeleted());
                    return definition;
                }).orElse(null);
        }
    }

    @NotNull
    private List<CellDataDto> getCellDataByType(List<CellDataDto> src, CellType type) {
        if (src == null) {
            return Collections.emptyList();
        }
        return src.stream().filter(cell -> Objects.equals(type, cell.getType())).toList();
    }
}
