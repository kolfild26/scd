package ru.sportmaster.scd.service.settings;

import ru.sportmaster.scd.dto.settings.AlgSettingsStatus;
import ru.sportmaster.scd.dto.settings.AlgType;

public interface AlgSettingLockService {
    AlgSettingsStatus getAlgSettingsInfo(AlgType type);

    void lock(AlgType type);

    void unlock(AlgType type);
}
