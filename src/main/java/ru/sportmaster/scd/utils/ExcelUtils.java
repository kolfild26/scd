package ru.sportmaster.scd.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtils {
    public static void setAutosizeColumnWidth(SXSSFSheet sheet) {
        Row row = sheet.iterator().next();
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i <= row.getLastCellNum(); i++) {
            sheet.autoSizeColumn(i, true);
        }
    }
}
