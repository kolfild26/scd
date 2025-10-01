package ru.sportmaster.scd.service.export;

import static java.lang.Math.min;
import static java.util.Objects.isNull;
import static java.util.stream.IntStream.range;
import static ru.sportmaster.scd.consts.ParamNames.PIVOT_LEVEL_COLUMN;
import static ru.sportmaster.scd.dto.pivot.DownloadType.EXCEL;
import static ru.sportmaster.scd.utils.ExcelUtils.setAutosizeColumnWidth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.DownloadType;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.entity.struct.TpPivotColumn;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;

@Component
@RequiredArgsConstructor
public class ExcelPivotExportDataService extends AbstractPivotExportDataService {
    private static final int BATCH_SIZE = 1000;
    private static final int MIN_COLUMN_CHARS_WIDTH = 12;
    private static final int MAX_EXCEL_GROUPS = 7;
    private static final double MAX_LIST_ROWS = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
    private final MessageSource messageSource;

    @Override
    public byte[] exportData(@NonNull PivotDownloadRequestDto request,
                             @NonNull TpPivotConfig config,
                             @NonNull ResultSet resultSet,
                             @NonNull Consumer<PivotPreparingFileDto> updateStatus
    ) throws IOException {
        setViewRequestOptions(request, config);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook())) {
            workbook.setCompressTempFiles(true);
            var sheetsCount = (int) Math.ceil(config.getTotal() / MAX_LIST_ROWS);
            var isOneSheet = config.getTotal() <= MAX_LIST_ROWS;
            var openedHierarchy = new HashMap<Integer, List<Object>>();
            var maxRowLevel = getMaxRowLevel(config);

            range(0, sheetsCount).forEach(sheetIndex -> {
                String sheetName = isOneSheet ? "Данные" : "Данные. Часть " + (sheetIndex + 1);
                SXSSFSheet sheet = workbook.createSheet(sheetName);
                sheet.setRandomAccessWindowSize(BATCH_SIZE);
                sheet.setDefaultColumnWidth(MIN_COLUMN_CHARS_WIDTH);
                sheet.setRowSumsBelow(false);

                var payload = ExcelPivotPayload.builder()
                    .resultSet(resultSet)
                    .sheet(sheet)
                    .config(config)
                    .isHierarchy(isHierarchy(request))
                    .openedHierarchy(openedHierarchy)
                    .maxLevel(maxRowLevel)
                    .build();

                writeHeaderLine(config, workbook, payload.getSheet());
                writeDataLines(payload, updateStatus, sheetIndex);
                setAutosizeColumnWidth(sheet);
            });

            openedHierarchy.clear();
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                workbook.dispose();
                return outputStream.toByteArray();
            }
        }
    }

    @Override
    public boolean isSupported(DownloadType type) {
        return EXCEL.equals(type);
    }

    @Override
    protected MessageSource getMessageSource() {
        return messageSource;
    }

    private void writeHeaderLine(@NonNull TpPivotConfig config,
                                 @NonNull SXSSFWorkbook workbook,
                                 @NonNull SXSSFSheet sheet) {
        var levelColumns = separateColumnByLevel(config.getColumns());
        var maxLevel = levelColumns.size() - 1;

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        int staticColumns = 0;
        for (int level = 0; level < levelColumns.size(); level++) {
            Row row = sheet.createRow(level);

            int columnIndex = staticColumns;
            for (TpPivotColumn column : levelColumns.get(level)) {
                createCell(row, columnIndex, headerStyle, column.getTitle());

                if (hasChildren(column)) {
                    var colMergeOffset = column.getChildrens().size() - 1;
                    if (colMergeOffset != 0) {
                        var startMergeIndex = columnIndex;
                        var endMergeIndex = columnIndex + colMergeOffset;
                        sheet.addMergedRegion(new CellRangeAddress(level, level, startMergeIndex, endMergeIndex));
                        columnIndex += colMergeOffset;
                    }
                } else if (level == 0 && maxLevel > 0) {
                    sheet.addMergedRegion(new CellRangeAddress(0, maxLevel, columnIndex, columnIndex));
                    staticColumns++;
                }

                columnIndex++;
            }
        }
    }

    private void createCell(Row row, int columnCount, CellStyle style, Object value) {
        Cell cell = row.createCell(columnCount);
        cell.setCellStyle(style);

        if (isNull(value)) {
            cell.setBlank();
        } else if (value instanceof Integer integerValue) {
            cell.setCellValue(integerValue);
        } else if (value instanceof Boolean booleanValue) {
            cell.setCellValue(booleanValue);
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    @SneakyThrows
    private void writeDataLines(@NonNull ExcelPivotPayload payload,
                                @NonNull Consumer<PivotPreparingFileDto> updateStatus,
                                int sheetIndex
    ) {
        var config = payload.getConfig();
        var sheet = payload.getSheet();
        var resultSet = payload.getResultSet();

        var columns = getDataColumns(config.getColumns());
        var lastCreatedGroupLevel = new AtomicInteger(-1);
        var index = new AtomicInteger(sheet.getLastRowNum() + 1);
        int sheetCount = sheetIndex + 1;

        if (payload.hasOpenedHierarchy()) {
            restoreOpenedHierarchy(payload, index, lastCreatedGroupLevel);
        }

        while (resultSet.next()) {
            int rowIndex = index.getAndIncrement();
            Row row = sheet.createRow(rowIndex);
            var values = new ArrayList<>();

            if (rowIndex % BATCH_SIZE == BATCH_SIZE - 1) {
                updateStatus.accept(buildStatus(rowIndex * sheetCount, config.getTotal()));
            }

            if (payload.isHierarchy()) {
                var level = resultSet.getInt(PIVOT_LEVEL_COLUMN);
                groupRow(sheet, lastCreatedGroupLevel, level, rowIndex);
            }

            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                TpPivotColumn column = columns.get(columnIndex);
                Object value = resultSet.getObject(column.getKey());
                createCell(row, columnIndex, null, value);
                values.add(value);
            }

            saveOpenedHierarchy(payload, values);

            if (rowIndex >= MAX_LIST_ROWS - 1) {
                return;
            }
        }
    }

    private void saveOpenedHierarchy(ExcelPivotPayload payload, List<Object> values) throws SQLException {
        if (payload.isHierarchy()) {
            var level = payload.getResultSet().getInt(PIVOT_LEVEL_COLUMN);

            if (level != payload.getMaxLevel()) {
                var openedHierarchy = payload.getOpenedHierarchy();
                openedHierarchy.put(level, values);
                if (level < openedHierarchy.size() - 1) {
                    range(level + 1, openedHierarchy.size()).forEach(openedHierarchy::remove);
                }
            }
        }
    }

    private void restoreOpenedHierarchy(
        ExcelPivotPayload payload,
        AtomicInteger index,
        AtomicInteger lastCreatedGroupLevel
    ) {
        var sheet = payload.getSheet();
        for (var entry : payload.getOpenedHierarchy().entrySet()) {
            var rowIndex = index.getAndIncrement();
            Row row = sheet.createRow(rowIndex);
            var level = entry.getKey();
            var values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                createCell(row, i, null, values.get(i));
            }

            groupRow(sheet, lastCreatedGroupLevel, level, rowIndex);
        }
    }

    private void groupRow(SXSSFSheet sheet, AtomicInteger lastCreatedGroupLevel, Integer level, int rowIndex) {
        // Создание группы excel, если она еще не создана.
        if (level <= MAX_EXCEL_GROUPS && level > lastCreatedGroupLevel.get()) {
            sheet.groupRow(rowIndex, rowIndex);
            lastCreatedGroupLevel.set(level);
        }

        // Добавление строки в группу, соответствующую её уровню.
        sheet.setRowOutlineLevel(rowIndex, min(level, MAX_EXCEL_GROUPS));
    }

    @Data
    @Builder
    private static class ExcelPivotPayload {
        @NonNull
        private ResultSet resultSet;
        @NonNull
        private TpPivotConfig config;
        @NonNull
        private SXSSFSheet sheet;
        @NonNull
        private Map<Integer, List<Object>> openedHierarchy;
        private boolean isHierarchy;
        private long maxLevel;

        public boolean hasOpenedHierarchy() {
            return isHierarchy && !openedHierarchy.isEmpty();
        }
    }
}
