package ru.sportmaster.scd.service.airflow;

import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Profile("!(prod | test)")
public class NotAirflowService implements IAirflowService {
    @Override
    public void finishAlgorithm(@NonNull Long algorithmId,
                                @NonNull Map<String, Object> params) {
        // В отсутствии AirFlow ничего не отправляем
    }
}
