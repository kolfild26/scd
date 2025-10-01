package ru.sportmaster.scd.service.export;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.consts.ParamNames.PIVOT_HIERARCHY_COLUMN;
import static ru.sportmaster.scd.consts.ParamNames.PIVOT_LEVEL_COLUMN;
import static ru.sportmaster.scd.dto.pivot.DownloadType.PDF;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
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
public class PDFPivotExportDataService extends AbstractPivotExportDataService {
    private static final Color HEADER_COLOR = new Color(212, 216, 227);
    private static final int COLUMN_WIDTH = 200;
    private static final float HIERARCHY_LEVEL_OFFSET = 22f;
    private static final int ROW_HEIGHT = 32;
    private static final int BATCH_SIZE = 1000;
    private static final String EMPTY_CELL = "";
    private final MessageSource messageSource;

    @Override
    public byte[] exportData(@NonNull PivotDownloadRequestDto request,
                             @NonNull TpPivotConfig config,
                             @NonNull ResultSet resultSet,
                             @NonNull Consumer<PivotPreparingFileDto> updateStatus
    ) throws SQLException, IOException {
        setViewRequestOptions(request, config);

        var dataColumns = getDataColumns(config.getColumns());
        var width = dataColumns.size() * COLUMN_WIDTH;
        var total = config.getTotal();

        Rectangle rectangle = new Rectangle(width, PageSize.TABLOID.getHeight());
        Document document = new Document(rectangle);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final var pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();
            var table = writeHeaderLine(config, dataColumns);
            writeDataLines(resultSet, document, table, total, dataColumns, isHierarchy(request), updateStatus);
            document.add(table);
            document.close();
            pdfWriter.close();
            return outputStream.toByteArray();
        }
    }

    @Override
    public boolean isSupported(DownloadType type) {
        return PDF.equals(type);
    }

    @Override
    protected MessageSource getMessageSource() {
        return messageSource;
    }

    private PdfPTable writeHeaderLine(
        @NonNull TpPivotConfig config,
        @NonNull List<TpPivotColumn> columns
    ) {
        var table = new PdfPTable(columns.size());
        table.setWidthPercentage(100f);
        table.setSpacingBefore(5);

        var levelColumns = separateColumnByLevel(config.getColumns());
        var maxLevel = levelColumns.size();
        table.setHeaderRows(levelColumns.size());

        for (int level = 0; level < levelColumns.size(); level++) {
            for (TpPivotColumn column : levelColumns.get(level)) {
                var cell = new PdfPCell();
                cell.setBackgroundColor(HEADER_COLOR);
                cell.setFixedHeight(ROW_HEIGHT);
                cell.setPadding(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(new Phrase(column.getTitle()));

                if (hasChildren(column)) {
                    var colMergeOffset = column.getChildrens().size();
                    if (colMergeOffset != 0) {
                        cell.setColspan(colMergeOffset);
                    }
                } else if (level == 0 && maxLevel > 1) {
                    cell.setRowspan(maxLevel);
                }

                table.addCell(cell);
            }
        }
        return table;
    }

    private void writeDataLines(@NonNull ResultSet resultSet,
                                @NonNull Document document,
                                @NonNull PdfPTable table,
                                @NonNull Long total,
                                @NonNull List<TpPivotColumn> columns,
                                @NonNull boolean isHierarchy,
                                @NonNull Consumer<PivotPreparingFileDto> updateStatus
    ) throws SQLException {
        int rowIndex = 0;
        while (resultSet.next()) {
            if (rowIndex % BATCH_SIZE == BATCH_SIZE - 1) {
                document.add(table);
                table.deleteBodyRows();
                updateStatus.accept(buildStatus(rowIndex, total));
            }
            for (TpPivotColumn column : columns) {
                Object value = resultSet.getObject(column.getKey());
                var cell = new PdfPCell();
                if (isHierarchy && PIVOT_HIERARCHY_COLUMN.equalsIgnoreCase(column.getKey())) {
                    var level = resultSet.getInt(PIVOT_LEVEL_COLUMN);
                    cell.setPaddingLeft(HIERARCHY_LEVEL_OFFSET * level);
                }
                cell.setPhrase(new Phrase(isNull(value) ? EMPTY_CELL : String.valueOf(value)));
                table.addCell(cell);
            }
            rowIndex++;
        }
    }
}
