package ru.sportmaster.scd.service.adjustment.io;

import static ru.sportmaster.scd.utils.JsonUtil.convertType;
import static ru.sportmaster.scd.utils.UiUtil.DATE_FORMAT;
import static ru.sportmaster.scd.utils.UiUtil.isDateOrTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.exceptions.BadRequestException;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowValuesRepository;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UIViewAttributeEditor;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JsonUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdjustmentDocumentReaderImpl implements AdjustmentDocumentReader {
    private final UiViewManager viewManager;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final AdjustmentDocRowValuesRepository adjustmentDocRowValuesRepository;

    @Override
    public List<ObjectNode> convert(AdjustmentType type, MultipartFile file) {
        return readExcel(type, file);
    }

    @Override
    public List<ObjectNode> convertToEntities(AdjustmentType type, List<ObjectNode> nodes) {
        List<ObjectNode> result = new ArrayList<>();
        if (nodes != null && !nodes.isEmpty()) {
            UiView view = viewManager.getView(type.getRowClass());
            List<UiViewAttribute> attributes = view.getAttributes();

            nodes.forEach(node -> {
                ObjectNode item = JsonNodeFactory.instance.objectNode();
                for (UiViewAttribute attribute : attributes) {
                    String fieldName = JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel());
                    UIViewAttributeEditor editor = attribute.getEditor();

                    if (editor != null) {
                        if (viewManager.isView(editor.getType())) {
                            UiView fieldView = viewManager.getView(editor.getType());
                            String attrName = attribute.getName().substring(
                                attribute.getName().lastIndexOf('.') + 1
                            );
                            JsonNode value = objectMapper.convertValue(adjustmentDocRowValuesRepository.getValue(
                                fieldView.getJavaType(),
                                attrName,
                                convertType(attribute.getJavaType(), node.get(fieldName))
                            ), JsonNode.class);
                            item.set(editor.getName(), value);
                        } else {
                            item.putPOJO(attribute.getName(), node.get(fieldName).asText());
                        }
                    } else if (isDateOrTime(attribute.getJavaType())) {
                        item.putPOJO(attribute.getName(), node.get(fieldName).asText());
                    } else {
                        Object value = convertType(attribute.getJavaType(), node.get(fieldName));
                        item.putPOJO(attribute.getName(), value);
                    }
                }
                result.add(item);
            });
        }
        return result;
    }

    @SneakyThrows
    private List<ObjectNode> readExcel(AdjustmentType type, MultipartFile file) {
        UiView view = viewManager.getView(type.getRowClass());
        List<ObjectNode> result = new ArrayList<>();

        try (InputStream stream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet sheet = workbook.getSheetAt(0);

            List<String> header = StreamSupport.stream(sheet.getRow(0).spliterator(), false)
                .map(Cell::getStringCellValue)
                .toList();

            if (view.getAttributes().size() < header.size()) {
                throw new BadRequestException("Ошибка: неверный формат. Число полей не совпадает с типом "
                    + "корректировки. Файл не может быть загружен.");
            }

            sheet.forEach(row -> {
                if (row.getRowNum() != 0) {
                    ObjectNode node = JsonNodeFactory.instance.objectNode();
                    for (int i = 0; i < header.size(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            putValue(node, header.get(i), cell);
                        } else {
                            node.putNull(header.get(i));
                        }
                    }
                    result.add(node);
                }
            });

            return result;
        } catch (POIXMLException | IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestException("Ошибка: неверное расширение. Файл не может быть загружен.", ex);
        }
    }

    private void putValue(ObjectNode node, String field, Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    node.put(field, cell.getLocalDateTimeCellValue().format(DATE_FORMAT));
                } else {
                    node.put(field, cell.getNumericCellValue());
                }
            }
            case STRING -> node.put(field, cell.getStringCellValue());
            case FORMULA -> node.put(field, cell.getCellFormula());
            case BOOLEAN -> node.put(field, cell.getBooleanCellValue());
            default -> node.putNull(field);
        }
    }
}
