package ru.sportmaster.scd.service.view;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.Status;
import ru.sportmaster.scd.dto.view.ViewDownloadRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.exceptions.BadRequestException;
import ru.sportmaster.scd.service.view.io.ViewExcelReader;
import ru.sportmaster.scd.service.view.io.ViewExcelWriter;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewFileServiceImpl implements ViewFileService {
    private static final ExecutorService VALIDATION_FILE_POOL = Executors.newWorkStealingPool();
    private static final String FILE_ERROR_MESSAGE = "File with uuid = ";

    private final HazelCastComponent hazelCastComponent;
    private final ViewExcelWriter viewExcelWriter;
    private final ViewExcelReader viewExcelReader;
    private final UiViewManager viewManager;

    private Map<String, ViewValidateFileResponseDto> validateMap;

    @PostConstruct
    public void init() {
        validateMap = hazelCastComponent.getViewFileUploadValidateMap();
    }

    @Override
    @Transactional
    public Resource download(ViewDownloadRequestDto request, String sessionUuid, String formUuid) {
        if (request.isTemplate()) {
            return new ByteArrayResource(viewExcelWriter.buildTemplate(request.getView()));
        } else {
            var resolver = viewManager.getResolver(request.getView());
            var rowsStream = resolver.findAllStream(buildFetchRequest(request), sessionUuid, formUuid);
            return new ByteArrayResource(viewExcelWriter.buildRows(request.getView(), rowsStream));
        }
    }

    @Override
    @SneakyThrows
    public String startValidate(String viewName, MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        var bytes = file.getBytes();

        VALIDATION_FILE_POOL.execute(() -> {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
                validateMap.put(uuid, ViewValidateFileResponseDto.start());
                var requests = viewExcelReader.readToChangeRequests(
                    viewName,
                    uuid,
                    stream,
                    status -> validateMap.put(uuid, status)
                );

                var status = validateMap.get(uuid);
                if (status.isValid()) {
                    validateMap.put(uuid, ViewValidateFileResponseDto.done(viewName, requests));
                } else {
                    validateMap.put(uuid, status.fileError());
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                validateMap.put(uuid, ViewValidateFileResponseDto.blockedError(ex.getMessage()));
            }
        });

        return uuid;
    }

    @Override
    public ViewValidateFileResponseDto validateStatus(String uuid) {
        var status = validateMap.get(uuid);
        if (isNull(status)) {
            throw new BadRequestException(FILE_ERROR_MESSAGE + uuid + " not found!");
        }
        if (status.getStatus() == Status.ERROR) {
            validateMap.remove(uuid);
        }

        return status;
    }

    @Override
    public void saveFileData(String uuid) {
        var status = validateMap.get(uuid);
        if (isNull(status)) {
            throw new BadRequestException(FILE_ERROR_MESSAGE + uuid + " not found!");
        }
        if (status.getStatus() == Status.QUEUE || status.getStatus() == Status.PROCESS) {
            throw new BadRequestException(FILE_ERROR_MESSAGE + uuid + " still processing!");
        }

        validateMap.remove(uuid);
        if (status.getStatus() == Status.ERROR) {
            throw new BadRequestException(FILE_ERROR_MESSAGE + uuid + " has errors!");
        }

        var editor = viewManager.getEditor(status.getView());
        var requests = status.getRequests();
        if (nonNull(requests) && !requests.isEmpty()) {
            requests.forEach(editor::change);
        }
    }

    private static UiViewFetchRequest buildFetchRequest(ViewDownloadRequestDto request) {
        return UiViewFetchRequest.builder()
            .view(request.getView())
            .conditions(request.getConditions())
            .memSelections(request.getMemSelections())
            .full(true)
            .build();
    }
}
