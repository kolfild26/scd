package ru.sportmaster.scd.service.export;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.VALUE;
import static ru.sportmaster.scd.consts.ParamNames.WEEK_VALUE_PREFIX;
import static ru.sportmaster.scd.utils.CollectionUtil.changeListByPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.entity.struct.TpPivotColumn;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;
import ru.sportmaster.scd.entity.struct.TpPivotConfigParams;

public abstract class AbstractPivotExportDataService implements PivotExportDataService {
    private static final String HIERARCHY_PIVOT_PREFIX = "HIER";

    protected abstract MessageSource getMessageSource();

    protected boolean isHierarchy(PivotDownloadRequestDto request) {
        return request.getPivotId().contains(HIERARCHY_PIVOT_PREFIX);
    }

    protected Map<Integer, List<TpPivotColumn>> separateColumnByLevel(List<TpPivotColumn> columns) {
        return buildColumnLevelMap(columns, 0, new HashMap<>());
    }

    protected long getMaxRowLevel(TpPivotConfig config) {
        return ofNullable(config.getParams())
            .map(TpPivotConfigParams::getHierarchy)
            .map(i -> i.stream()
                .filter(j -> nonNull(j.getIsSelected()) && j.getIsSelected() == 1)
                .count())
            .orElse(0L);
    }

    protected Map<Integer, List<TpPivotColumn>> buildColumnLevelMap(
        List<TpPivotColumn> columns, int level, Map<Integer, List<TpPivotColumn>> result
    ) {
        for (TpPivotColumn column : columns) {
            var list = result.computeIfAbsent(level, k -> new ArrayList<>());
            boolean isVisible = isNull(column.getIsHidden()) || column.getIsHidden() == 0;
            if (isVisible) {
                list.add(column);

                if (hasChildren(column)) {
                    buildColumnLevelMap(column.getChildrens(), level + 1, result);
                }
            }
        }

        return result;
    }

    protected List<TpPivotColumn> getDataColumns(List<TpPivotColumn> columns) {
        return separateColumnByLevel(columns).values().stream()
            .flatMap(List::stream)
            .filter(i -> !hasChildren(i))
            .toList();
    }

    protected boolean hasChildren(TpPivotColumn column) {
        return nonNull(column.getChildrens()) && !column.getChildrens().isEmpty();
    }

    protected PivotPreparingFileDto buildStatus(int current, long total) {
        var progress = ((float) current / total) * 100;
        return PivotPreparingFileDto.process(progress);
    }

    protected void setViewRequestOptions(PivotDownloadRequestDto request, TpPivotConfig config) {
        if (isNull(request)) {
            return;
        }

        if (nonNull(request.getHiddenColumnKeys())) {
            var hiddenColumns = request.getHiddenColumnKeys();
            changeListByPredicate(
                config.getColumns(),
                col -> hiddenColumns.contains(col.getKey()),
                col -> col.setIsHidden(1)
            );
        }

        if (nonNull(request.getDataColumnOptions())) {
            var dataOptions = request.getDataColumnOptions();

            if (!dataOptions.contains(VALUE)) {
                changeListByPredicate(
                    config.getColumns(),
                    col -> hasChildren(col) || col.getKey().startsWith(WEEK_VALUE_PREFIX),
                    col -> col.setIsHidden(1)
                );
            }

            var firstValueColumn = findColumnIndex(config.getColumns());
            for (String fieldKey : dataOptions) {
                if (!fieldKey.equalsIgnoreCase(VALUE)) {
                    config.getColumns().add(firstValueColumn, TpPivotColumn.builder()
                        .key(fieldKey)
                        .title(getTitle(fieldKey))
                        .isHidden(0)
                        .build());
                    firstValueColumn++;
                }
            }
        }
    }

    private int findColumnIndex(List<TpPivotColumn> list) {
        return list.stream()
            .filter(this::hasChildren).findFirst()
            .map(list::indexOf)
            .orElse(list.size() - 1);
    }

    private String getTitle(String key) {
        return getMessageSource().getMessage(key, null, null, LocaleContextHolder.getLocale());
    }
}
