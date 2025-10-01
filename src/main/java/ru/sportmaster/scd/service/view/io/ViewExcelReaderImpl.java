package ru.sportmaster.scd.service.view.io;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.StreamSupport.stream;
import static ru.sportmaster.scd.ui.view.type.UIViewConditionOperation.EQ;
import static ru.sportmaster.scd.utils.ClassUtils.getAnnotation;
import static ru.sportmaster.scd.utils.CollectionUtil.join;
import static ru.sportmaster.scd.utils.ConvertUtil.getDateString;
import static ru.sportmaster.scd.utils.JpaUtil.getValue;
import static ru.sportmaster.scd.utils.JpaUtil.isEmbeddable;
import static ru.sportmaster.scd.utils.JsonUtil.convertExcelRowToObjectNode;
import static ru.sportmaster.scd.utils.JsonUtil.convertType;
import static ru.sportmaster.scd.utils.JsonUtil.getLocalizedMessage;
import static ru.sportmaster.scd.utils.UiUtil.isDate;
import static ru.sportmaster.scd.utils.UiUtil.safeToString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.exceptions.BadRequestException;
import ru.sportmaster.scd.service.view.io.preprocessor.ViewFileRequestBuilderPreprocessor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewFileUpdate;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;
import ru.sportmaster.scd.utils.JsonUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewExcelReaderImpl implements ViewExcelReader {
    private static final int BATCH_SIZE = 100;
    private static final BiPredicate<Integer, Integer> IS_UPDATE_STATUS = (index, total) -> {
        if (total > BATCH_SIZE) {
            return index % BATCH_SIZE == BATCH_SIZE - 1;
        }

        return true;
    };

    private final UiViewManager viewManager;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final ViewRequestBuilder viewRequestBuilder;
    private final List<ViewFileRequestBuilderPreprocessor> registerPreprocessors;

    private Map<Class<?>, List<ViewFileRequestBuilderPreprocessor>> preprocessors;

    @PostConstruct
    public void init() {
        preprocessors = registerPreprocessors.stream().collect(
            groupingBy(ViewFileRequestBuilderPreprocessor::getTargetClass)
        );
    }

    @Override
    @Transactional
    public List<UiViewCrudRequest> readToChangeRequests(
        String viewName, String uuid, InputStream stream, Consumer<ViewValidateFileResponseDto> updateStatus
    ) {
        UiView view = viewManager.getView(viewName);
        if (isNull(view)) {
            throw new BadRequestException("Ошибка: данный тип view не найден. Файл не может быть загружен.");
        }

        ViewFileUpdate updateRules = getAnnotation(view.getJavaType(), View.class)
            .map(View::fileUpdate)
            .map(i -> i[0])
            .orElseThrow(() ->
                new BadRequestException("Ошибка: данный тип view не может быть отредактирован через excel. "
                    + "Файл не может быть загружен.")
            );

        String idLabel = getLocalizedMessage(messageSource, view.getIdField());
        var viewPreprocessors = preprocessors.get(view.getJavaType());
        List<UiViewCrudRequest> result = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(stream)) {
            Sheet sheet = workbook.getSheetAt(0);

            var headerRow = sheet.getRow(0).spliterator();
            var fileHeaders = stream(headerRow, false).map(Cell::getStringCellValue).toList();
            var fileAttributes = fileHeaders.stream()
                .map(label -> findViewAttribute(label, idLabel, view))
                .toList();

            if (!isEqualHeaders(fileHeaders, view.getAttributes())) {
                throw new BadRequestException("Ошибка: неверный формат. Число полей не совпадает с типом view. "
                    + "Файл не может быть загружен.");
            }

            var resolver = viewManager.getResolver(viewName);
            var iterator = sheet.rowIterator();
            int total = sheet.getLastRowNum();
            int index = 0;
            int errorCount = 0;

            updateStatus.accept(buildStatus(index, total, errorCount));
            // Skip header
            iterator.next();

            while (iterator.hasNext()) {
                var row = iterator.next();
                var changed = convertExcelRowToObjectNode(row, fileAttributes, objectMapper);
                applyPreprocessorsOnRow(viewPreprocessors, uuid, changed);

                var id = JsonUtil.getId(view.getJavaType(), changed);
                var savedEntity = findById(resolver, view, changed.get(view.getIdField()));

                try {
                    if (!validateRow(viewPreprocessors, uuid, changed)) {
                        errorCount++;
                    } else {
                        if (isNull(id) || isNull(savedEntity)) {
                            if (updateRules.creatable()) {
                                result.add(viewRequestBuilder.buildCreateRequest(view, changed));
                            } else {
                                errorCount++;
                            }
                        } else if (!isEqualEntity(savedEntity, changed, fileAttributes, viewPreprocessors, uuid)) {
                            if (updateRules.updatable()) {
                                result.add(viewRequestBuilder.buildUpdateRequest(view, changed, savedEntity));
                            } else {
                                errorCount++;
                            }
                        }
                    }
                } catch (BadRequestException ex) {
                    errorCount++;
                }

                if (IS_UPDATE_STATUS.test(index, total)) {
                    updateStatus.accept(buildStatus(index, total, errorCount));
                }

                index++;
            }


            clearPreprocessors(viewPreprocessors, uuid);
            return result;
        } catch (POIXMLException | IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestException("Ошибка: неверное расширение. Файл не может быть загружен.", ex);
        }
    }

    private boolean isEqualHeaders(List<String> fileHeaders, List<UiViewAttribute> attributes) {
        if (attributes.size() == fileHeaders.size() - 1) {
            var viewLabels = attributes.stream().map(this::getLabel).collect(Collectors.toList());
            viewLabels.removeAll(fileHeaders);

            return viewLabels.isEmpty();
        }

        return false;
    }

    private boolean isEqualEntity(Object saved,
                                  ObjectNode changed,
                                  List<UiViewAttribute> attributes,
                                  List<ViewFileRequestBuilderPreprocessor> preprocessors,
                                  String uuid) {
        var equal = true;

        for (UiViewAttribute attribute : attributes) {
            var changedField = convertChangedKeyToString(changed, attribute);
            var savedField = convertSavedKeyToString(saved, attribute);

            if (!Objects.equals(changedField, savedField)) {
                if (equal) {
                    equal = false;
                }

                boolean isAllowEdit = attribute.isEditable() || isAllowRowEdit(preprocessors, uuid, changed);
                if (!isAllowEdit) {
                    throw new BadRequestException(
                        "Ошибка: изменено значение не редактируемого поля. Файл не может быть загружен."
                    );
                }
            }
        }

        return equal;
    }

    private String getLabel(UiViewAttribute attribute) {
        return getLocalizedMessage(messageSource, attribute.getLabel());
    }

    private UiViewAttribute findViewAttribute(String label, String idLabel, UiView view) {
        if (label.equalsIgnoreCase(idLabel)) {
            return UiViewAttribute.builder()
                .name(view.getIdField())
                .label(LocalizedProperty.of(idLabel))
                .javaType(view.getIdFieldType())
                .build();
        }

        return view.getAttributes().stream()
            .filter(attr -> label.equalsIgnoreCase(getLabel(attr)))
            .findFirst()
            .orElse(null);
    }

    private String convertSavedKeyToString(Object obj, UiViewAttribute attribute) {
        var fieldValue = getValue(obj, attribute.getName());

        if (isNull(fieldValue)) {
            return null;
        } else if (isDate(fieldValue)) {
            return getDateString(fieldValue);
        } else if (isEmbeddable(attribute.getJavaType())) {
            return objectMapper.convertValue(fieldValue, ObjectNode.class).toString();
        } else {
            return safeToString(fieldValue);
        }
    }

    private String convertChangedKeyToString(JsonNode node, UiViewAttribute attribute) {
        if (isEmbeddable(attribute.getJavaType())) {
            return node.get(attribute.getName()).toString();
        }

        Object val = convertType(attribute.getJavaType(), node.get(attribute.getName()));
        return isNull(val) ? null : val.toString();
    }

    private ViewValidateFileResponseDto buildStatus(int current, int total, int errorCount) {
        var progress = ((float) current / total) * 100;
        return ViewValidateFileResponseDto.progress(progress, errorCount);
    }

    private Object findById(UiViewResolver resolver, UiView view, JsonNode id) {
        List<ICondition> conditions = new ArrayList<>();

        if (view.isEmbeddedId()) {
            id.fieldNames().forEachRemaining(field ->
                conditions.add(UIViewCondition.builder()
                    .field(join(view.getIdField(), field))
                    .operation(EQ)
                    .value(id.get(field))
                    .build()));
        } else {
            conditions.add(UIViewCondition.builder()
                .field(view.getIdField())
                .operation(EQ)
                .value(id)
                .build());
        }


        return resolver.findOne(
            UiViewFetchRequest.builder()
                .view(view.getType())
                .size(1)
                .conditions(conditions)
                .build());
    }

    private void applyPreprocessorsOnRow(List<ViewFileRequestBuilderPreprocessor> list, String uuid, ObjectNode node) {
        ofNullable(list).ifPresent(i -> i.forEach(j -> j.apply(uuid, node)));
    }

    private boolean validateRow(List<ViewFileRequestBuilderPreprocessor> list, String uuid, ObjectNode node) {
        return ofNullable(list).map(i -> i.stream().allMatch(j -> j.validate(uuid, node))).orElse(true);
    }

    private boolean isAllowRowEdit(List<ViewFileRequestBuilderPreprocessor> list, String uuid, ObjectNode node) {
        return list.stream().anyMatch(i -> i.isEditable(uuid, node));
    }

    private void clearPreprocessors(List<ViewFileRequestBuilderPreprocessor> list, String uuid) {
        ofNullable(list).ifPresent(i -> i.forEach(j -> j.clear(uuid)));
    }
}
