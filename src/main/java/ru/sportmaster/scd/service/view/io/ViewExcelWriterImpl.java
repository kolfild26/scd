package ru.sportmaster.scd.service.view.io;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.utils.ExcelUtils.setAutosizeColumnWidth;
import static ru.sportmaster.scd.utils.UiUtil.DATE_FORMAT;
import static ru.sportmaster.scd.utils.UiUtil.isDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JpaUtil;
import ru.sportmaster.scd.utils.JsonUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewExcelWriterImpl implements ViewExcelWriter {
    private static final int BATCH_SIZE = 1000;

    private final UiViewManager viewManager;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    @Override
    public byte[] buildTemplate(String viewName) {
        UiView view = viewManager.getView(viewName);
        List<UiViewAttribute> attributes = new ArrayList<>(view.getAttributes());
        putIdField(attributes, view);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook())) {
            SXSSFSheet sheet = workbook.createSheet(viewName);
            sheet.setColumnHidden(0, true);
            Row header = sheet.createRow(0);
            writeHeader(attributes, header);

            setAutosizeColumnWidth(sheet);
            return writeWorkbook(workbook);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            return new byte[0];
        }
    }

    private static void putIdField(List<UiViewAttribute> attributes, UiView view) {
        attributes.add(0, UiViewAttribute.builder()
            .name(view.getIdField())
            .label(LocalizedProperty.of(view.getIdField()))
            .javaType(view.isEmbeddedId() ? JsonNode.class : String.class)
            .build());
    }

    @Override
    public byte[] buildRows(String viewName, Stream<?> rows) {
        UiView view = viewManager.getView(viewName);
        List<UiViewAttribute> attributes = new ArrayList<>(view.getAttributes());
        putIdField(attributes, view);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook())) {
            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = workbook.createSheet(view.getType());
            sheet.setRandomAccessWindowSize(BATCH_SIZE);
            sheet.setColumnHidden(0, true);

            Row header = sheet.createRow(0);
            writeHeader(attributes, header);
            writeRows(rows, sheet, attributes);

            setAutosizeColumnWidth(sheet);
            return writeWorkbook(workbook);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            return new byte[0];
        }
    }

    private void writeRows(Stream<?> rows, SXSSFSheet sheet, List<UiViewAttribute> attributes) {
        AtomicInteger index = new AtomicInteger(1);

        rows.forEach(row -> {
            Row excelRow = sheet.createRow(index.getAndIncrement());

            for (int j = 0; j < attributes.size(); j++) {
                UiViewAttribute attribute = attributes.get(j);
                Cell cell = excelRow.createCell(j);
                Object value = JpaUtil.getValue(row, attribute.getName());
                if (isNull(value)) {
                    cell.setBlank();
                } else {
                    cell.setCellValue(convertToString(attribute.getJavaType(), value));
                }
            }
        });
    }

    private static byte[] writeWorkbook(SXSSFWorkbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.dispose();
            return outputStream.toByteArray();
        }
    }

    private void writeHeader(List<UiViewAttribute> attributes, Row header) {
        if (attributes != null && !attributes.isEmpty()) {
            for (int i = 0; i < attributes.size(); i++) {
                UiViewAttribute attribute = attributes.get(i);

                Cell cell = header.createCell(i);
                cell.setCellValue(JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel()));
            }
        }
    }

    private String convertToString(Class<?> clazz, Object value) {
        if (isDate(clazz)) {
            return LocalDate.from((TemporalAccessor) value).format(DATE_FORMAT);
        }
        if (clazz == JsonNode.class) {
            return objectMapper.convertValue(value, ObjectNode.class).toString();
        }

        return value.toString();
    }


}
