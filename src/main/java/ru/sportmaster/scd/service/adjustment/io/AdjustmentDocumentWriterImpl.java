package ru.sportmaster.scd.service.adjustment.io;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.utils.UiUtil.DATE_FORMAT;
import static ru.sportmaster.scd.utils.UiUtil.isDate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.dictionary.Week;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JpaUtil;
import ru.sportmaster.scd.utils.JsonUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdjustmentDocumentWriterImpl implements AdjustmentDocumentWriter {
    private final UiViewManager viewManager;
    private final MessageSource messageSource;

    @Override
    public byte[] buildTemplate(AdjustmentType type) {
        UiView view = viewManager.getView(type.getRowClass());
        List<UiViewAttribute> attributes = view.getAttributes();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(view.getType());
            Row header = sheet.createRow(0);
            writeHeader(attributes, sheet, header);

            return writeWorkbook(workbook);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            return new byte[0];
        }
    }

    @Override
    public byte[] buildDocument(AdjustmentType type, List<Object> rows) {
        UiView view = viewManager.getView(type.getRowClass());
        List<UiViewAttribute> attributes = view.getAttributes();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(view.getType());
            Row header = sheet.createRow(0);
            writeHeader(attributes, sheet, header);

            for (int i = 0; i < rows.size(); i++) {
                Row row = sheet.createRow(i + 1);

                for (int j = 0; j < attributes.size(); j++) {
                    UiViewAttribute attribute = attributes.get(j);
                    Cell cell = row.createCell(j);
                    Object value = JpaUtil.getValue(rows.get(i), attribute.getName());
                    if (isNull(value)) {
                        cell.setBlank();
                    } else {
                        cell.setCellValue(convertToString(attribute.getJavaType(), value));
                    }
                }
            }

            return writeWorkbook(workbook);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            return new byte[0];
        }
    }

    private static byte[] writeWorkbook(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void writeHeader(List<UiViewAttribute> attributes, Sheet sheet, Row header) {
        if (attributes != null && !attributes.isEmpty()) {
            for (int i = 0; i < attributes.size(); i++) {
                sheet.setColumnWidth(i, 5000);

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
        if (Week.class == clazz) {
            return ((Week) value).getName().format(DATE_FORMAT);
        }
        if (Day.class == clazz) {
            return ((Day) value).getName().format(DATE_FORMAT);
        }
        return value.toString();
    }
}
