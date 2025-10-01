package ru.sportmaster.scd.service.editor;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.dto.adjustment.editor.AlgEditorSchemaDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellDataDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellType;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.mapper.editor.AlgEditorMapper;
import ru.sportmaster.scd.repository.algorithms.definitions.AlgorithmDefinitionRepository;
import ru.sportmaster.scd.repository.algorithms.definitions.AlgorithmStageDefinitionRepository;
import ru.sportmaster.scd.repository.algorithms.definitions.AlgorithmStepDefinitionRepository;
import ru.sportmaster.scd.web.UserEnvironment;

@Service
@RequiredArgsConstructor
public class AlgEditorServiceImpl implements AlgEditorService {
    private final AlgorithmDefinitionRepository algorithmDefinitionRepository;
    private final AlgorithmStageDefinitionRepository algorithmStageRepository;
    private final AlgorithmStepDefinitionRepository algorithmStepRepository;
    private final AlgEditorMapper algEditorMapper;

    @Override
    @Transactional
    public List<AlgEditorSchemaDto> getSchemes() {
        List<AlgorithmDefinition> definitions = algorithmDefinitionRepository.findAllNotDeleted();
        return definitions.stream().map(i -> AlgEditorSchemaDto.builder()
                .id(i.getId())
                .name(i.getName())
                .description(i.getDescription())
                .defaultParams(i.getDefaultParams())
                .cellData(algEditorMapper.toDtos(i))
                .build()
        ).toList();
    }

    @Override
    public List<CellDataDto> getCellData(CellType type) {
        return switch (type) {
            case STAGE -> algorithmStageRepository.findAll().stream().map(algEditorMapper::mapStage).toList();
            case STEP -> algorithmStepRepository.findAll().stream().map(algEditorMapper::mapStep).toList();
        };
    }

    @Override
    @Transactional
    public AlgEditorSchemaDto saveSchema(AlgEditorSchemaDto schema, UserEnvironment environment) {
        AlgorithmDefinition definition = algEditorMapper.toEntity(schema);
        if (definition.getOwner() == null) {
            definition.setOwner(environment.getOsUser());
        }

        algorithmDefinitionRepository.saveOrUpdate(definition, definition.getId());
        algorithmDefinitionRepository.flush();

        schema.setId(definition.getId());
        schema.setChanges(null);
        schema.setCellData(algEditorMapper.toDtos(definition));
        return schema;
    }

    @Override
    public void deleteSchema(Long id) {
        AlgorithmDefinition definition = algorithmDefinitionRepository.findById(id);
        if (definition.getAlgorithmStageDefinitions() != null) {
            definition.getAlgorithmStageDefinitions().forEach(stage -> {
                if (stage.getAlgorithmStepDefinitions() != null) {
                    stage.getAlgorithmStepDefinitions().forEach(step -> step.setIsDeleted(true));
                }
                stage.setIsDeleted(true);
            });
        }
        definition.setIsDeleted(true);
        algorithmDefinitionRepository.merge(definition);
    }
}
