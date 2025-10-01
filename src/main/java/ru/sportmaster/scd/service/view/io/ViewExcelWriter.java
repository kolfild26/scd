package ru.sportmaster.scd.service.view.io;

import java.util.stream.Stream;

public interface ViewExcelWriter {
    byte[] buildTemplate(String viewName);

    byte[] buildRows(String viewName, Stream<?> rows);
}
