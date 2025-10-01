package ru.sportmaster.scd.mementity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.mementity.MemEntitySelectDto;
import ru.sportmaster.scd.dto.view.SelectionRequestDto;
import ru.sportmaster.scd.ui.view.UiViewResolver;

public interface IMemEntityResolver extends UiViewResolver, IMemEntitySearchProcessor {
    String getName();

    void initTopic(@NonNull HazelcastInstance hazelcastInstance);

    void onMessage(ObjectNode message);

    void exec(String sessionUuid, String formUuid, SelectionRequestDto request);

    MemEntitySelectDto<?, ?> getSelectionInfo(String sessionUuid, String formUuid);
}
