package ru.sportmaster.scd.service.export;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.pivot.DownloadType;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;

public interface PivotExportDataService {
    byte[] exportData(
        @NonNull PivotDownloadRequestDto request,
        @NonNull TpPivotConfig config,
        @NonNull ResultSet resultSet,
        @NonNull Consumer<PivotPreparingFileDto> updateStatus
    ) throws SQLException, IOException;

    boolean isSupported(DownloadType type);
}
