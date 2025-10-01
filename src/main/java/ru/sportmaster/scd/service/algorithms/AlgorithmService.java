package ru.sportmaster.scd.service.algorithms;

import java.util.Map;
import org.springframework.lang.NonNull;

public interface AlgorithmService {
    void execute(@NonNull Long algorithmId,
                 @NonNull Map<String, Object> params);
}
