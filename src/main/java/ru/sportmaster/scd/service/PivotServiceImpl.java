package ru.sportmaster.scd.service;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.dto.Status;
import ru.sportmaster.scd.dto.pivot.PivotConfigDto;
import ru.sportmaster.scd.dto.pivot.PivotConfigParametersDto;
import ru.sportmaster.scd.dto.pivot.PivotConfigStatusDto;
import ru.sportmaster.scd.dto.pivot.PivotDataRequest;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotFilterDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.dto.pivot.PivotSearchDataRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotValidationDataRequestDto;
import ru.sportmaster.scd.exceptions.FileBuildingNotCompleted;
import ru.sportmaster.scd.exceptions.FileNotFoundException;
import ru.sportmaster.scd.mapper.struct.TpPivotConfigMapper;
import ru.sportmaster.scd.mapper.struct.TpPivotConfigParamsMapper;
import ru.sportmaster.scd.mapper.struct.TpPivotFilterMapper;
import ru.sportmaster.scd.service.pivot.PivotDAOService;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Service
@RequiredArgsConstructor
public class PivotServiceImpl implements PivotService {
    private static final Map<String, PivotConfigStatusDto> TEMP_STATUSES = new HashMap<>();
    private static final ExecutorService PREPARE_DOWNLOAD_FILE_POOL = Executors.newWorkStealingPool();
    private final PivotDAOService pivotDAOService;
    private final HazelCastComponent hazelCastComponent;
    private final TpPivotConfigMapper configMapper;
    private final TpPivotConfigParamsMapper paramsMapper;
    private final TpPivotFilterMapper filterMapper;

    @Override
    public PivotConfigDto getPivotConfig(String pivotId, UserEnvironment environment) {
        return
            configMapper.toDto(
                pivotDAOService.getPivotConfig(
                    pivotId,
                    environment.getUserId()
                )
            );
    }

    @Override
    public void savePivotConfig(
        String pivotId,
        PivotConfigParametersDto parameters,
        UserEnvironment environment) {
        ForkJoinPool.commonPool().execute(() -> {
            try {
                TEMP_STATUSES.put(pivotId, PivotConfigStatusDto.builder().status(Status.PROCESS).build());
                pivotDAOService.savePivotConfig(
                    pivotId,
                    environment.getUserId(),
                    paramsMapper.fromDto(parameters)
                );
                TEMP_STATUSES.put(pivotId, PivotConfigStatusDto.builder().status(Status.DONE).build());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                TEMP_STATUSES.put(pivotId, PivotConfigStatusDto.builder().status(Status.ERROR).build());
            }
        });

    }

    @Override
    public PivotConfigStatusDto getSavePivotConfigStatus(String pivotId, UserEnvironment environment) {
        PivotConfigStatusDto status = TEMP_STATUSES.get(pivotId);
        status.setProgress(pivotDAOService.getProgressPivot(pivotId, environment.getUserId()));
        return status;
    }

    @Override
    public List<Object[]> getPivotData(PivotDataRequest request, UserEnvironment environment) {
        return
            pivotDAOService.getPivotData(
                request.getPivotId(),
                environment.getUserId(),
                request.getOffset() == 0 ? 0 : request.getOffset() + 1,
                request.getOffset() + request.getLimit()
            );
    }

    @Override
    public List<String> getPivotDataString(PivotDataRequest request, UserEnvironment environment) {
        return
            pivotDAOService.getPivotDataString(
                request.getPivotId(),
                environment.getUserId(),
                request.getOffset() == 0 ? 0 : request.getOffset() + 1,
                request.getOffset() + request.getLimit()
            );
    }

    @Override
    public List<JsonNode> getPivotDataJson(PivotDataRequest request, UserEnvironment environment) {
        return
            pivotDAOService.getPivotDataJson(
                request.getPivotId(),
                environment.getUserId(),
                request.getOffset() == 0 ? 0 : request.getOffset() + 1,
                request.getOffset() + request.getLimit(),
                request.getCollapsedOrderRows()
            );
    }

    @Override
    public List<Long> searchPivotRows(PivotSearchDataRequestDto request, UserEnvironment environment) {
        return
            pivotDAOService.searchPivotRows(
                request.getPivotId(),
                environment.getUserId(),
                request.getText()
            );
    }

    @Override
    public List<Long> validatePivotRows(PivotValidationDataRequestDto request, UserEnvironment environment) {
        return List.of(5L, 1000L, 20000L, 50000L, 100000L);
    }

    @Override
    public String startPivotPreparingForDownload(
        PivotDownloadRequestDto request, UserEnvironment environment
    ) {
        String uuid = UUID.randomUUID().toString();
        hazelCastComponent.getPivotFilePreparingMap().put(uuid, PivotPreparingFileDto.start());

        PREPARE_DOWNLOAD_FILE_POOL.execute(() ->
            pivotDAOService.downloadPivot(
                request,
                environment.getUserId(),
                status -> hazelCastComponent.getPivotFilePreparingMap().put(uuid, status)
            )
        );

        return uuid;
    }

    @Override
    public PivotPreparingFileDto checkPreparePivotStatus(String uuid) {
        return hazelCastComponent.getPivotFilePreparingMap().get(uuid);
    }

    @Override
    public byte[] downloadPivotFile(String uuid) {
        var status = ofNullable(hazelCastComponent.getPivotFilePreparingMap().get(uuid))
            .orElseThrow(() -> new FileNotFoundException(uuid));
        boolean isError = Status.ERROR == status.getStatus();
        if (isError || Status.DONE == status.getStatus()) {
            hazelCastComponent.getPivotFilePreparingMap().remove(uuid);
        } else {
            throw new FileBuildingNotCompleted();
        }
        if (isError) {
            throw status.getException();
        }

        return status.getData();
    }

    @Override
    public List<PivotFilterDto> getFilterSets(String pivotId, UserEnvironment environment) {
        return
            ofNullable(pivotDAOService.getFilterSets(pivotId, environment.getUserId()))
                .map(filters -> filters.stream().map(filterMapper::toDto).toList())
                .orElse(null);
    }

    @Override
    public void saveFilterSet(String pivotId, PivotFilterDto request, UserEnvironment environment) {
        pivotDAOService.saveFilterSet(pivotId, environment.getUserId(), filterMapper.fromDto(request));
    }

    @Override
    public void deleteFilterSets(String pivotId, List<String> names, UserEnvironment environment) {
        if (names != null && !names.isEmpty()) {
            pivotDAOService.deleteFilterSets(pivotId, environment.getUserId(), names);
        }
    }

    @Override
    public List<Object> getUniqFieldValues(String pivotId, String field, UserEnvironment environment) {
        return pivotDAOService.getUniqFieldValues(pivotId, field, environment.getUserId());
    }
}
