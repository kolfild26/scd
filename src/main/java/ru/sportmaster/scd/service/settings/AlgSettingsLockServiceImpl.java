package ru.sportmaster.scd.service.settings;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.dto.UserDto;
import ru.sportmaster.scd.dto.settings.AlgSettingsStatus;
import ru.sportmaster.scd.dto.settings.AlgType;
import ru.sportmaster.scd.dto.settings.LockType;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;
import ru.sportmaster.scd.exceptions.NotPermittedActionException;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmRepository;
import ru.sportmaster.scd.service.AuthService;
import ru.sportmaster.scd.task.HazelCastComponent;

@Service
@RequiredArgsConstructor
public class AlgSettingsLockServiceImpl implements AlgSettingLockService {
    private final AuthService authService;
    private final HazelCastComponent hazelCastComponent;
    private final AlgorithmRepository algorithmRepository;


    @Override
    public AlgSettingsStatus getAlgSettingsInfo(AlgType type) {
        LocalDateTime current = LocalDateTime.now();
        long algDefinitionId = type.getAlgorithmDefId();
        AlgSettingsStatus status = hazelCastComponent.getAlgSettingsLockMap().get(type);

        if (status == null) {
            status = checkAlgorithmIsRunning(type, algDefinitionId);
        } else {
            boolean timeoutIsOut = current.minusMinutes(10).isAfter(status.getStartTime());
            // Если пользователь покинул форму без сохранения, ждем тайм-аут и удаляем блокировку.
            if (LockType.SETTINGS_BEING_EDITED == status.getType() && timeoutIsOut) {
                hazelCastComponent.getAlgSettingsLockMap().remove(type);
                return null;
            } else if (LockType.ALGORITHM_IS_RUNNING == status.getType()
                && (status.getUnlockTime() == null || current.isAfter(status.getUnlockTime()))) {
                // Проверка если алгоритм вышел за рамки прогнозируемой длительности
                return checkAlgorithmIsRunning(type, algDefinitionId);
            }
        }

        return status;
    }

    @Override
    public void lock(AlgType type) {
        UserDto user = authService.getCurrentUser();
        AlgSettingsStatus status = AlgSettingsStatus.builder()
                .type(LockType.SETTINGS_BEING_EDITED)
                .startTime(LocalDateTime.now())
                .user(user)
                .build();

        hazelCastComponent.getAlgSettingsLockMap().put(type, status);
    }

    @Override
    public void unlock(AlgType type) {
        UserDto user = authService.getCurrentUser();
        AlgSettingsStatus status = hazelCastComponent.getAlgSettingsLockMap().get(type);
        if (status != null) {
            if (Objects.equals(user.getLogin(), status.getUser().getLogin())) {
                hazelCastComponent.getAlgSettingsLockMap().remove(type);
            } else {
                throw new NotPermittedActionException("Insufficient permission to unlock.");
            }
        }
    }

    private AlgSettingsStatus checkAlgorithmIsRunning(AlgType type, long algDefinitionId) {
        AlgSettingsStatus status = null;

        Algorithm runningAlgorithm = algorithmRepository.getLastAlgorithm(algDefinitionId, false);
        if (runningAlgorithm != null) {
            status = AlgSettingsStatus.builder()
                .type(LockType.ALGORITHM_IS_RUNNING)
                .startTime(ofNullable(runningAlgorithm.getStartTime()).orElse(runningAlgorithm.getCreateTime()))
                .unlockTime(getPredictedDuration(runningAlgorithm, algDefinitionId))
                .build();

            hazelCastComponent.getAlgSettingsLockMap().put(type, status);
        } else {
            hazelCastComponent.getAlgSettingsLockMap().remove(type);
        }

        return status;
    }

    private LocalDateTime getPredictedDuration(@NotNull Algorithm running, long algDefId) {
        Long duration = algorithmRepository.getAlgDuration(algDefId);
        if (duration != null) {
            var startTime = ofNullable(running.getStartTime()).orElse(running.getCreateTime());
            return startTime.plus(duration, ChronoUnit.MILLIS);
        }
        return null;
    }
}
