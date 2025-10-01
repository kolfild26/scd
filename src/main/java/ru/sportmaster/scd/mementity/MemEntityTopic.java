package ru.sportmaster.scd.mementity;

import static ru.sportmaster.scd.consts.ParamNames.PREFIX_MEM_ENTITY_TOPIC_NAME;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hazelcast.config.Config;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemEntityTopic implements MessageListener<ObjectNode> {
    private static final String TOPIC_CONFIG_BLANK = PREFIX_MEM_ENTITY_TOPIC_NAME + "*";

    private final List<IMemEntityResolver> memEntityResolvers;

    public void setConfig(@NonNull Config config) {
        log.debug("MemEntityTopic конфигурация");
        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setGlobalOrderingEnabled(false);
        topicConfig.setStatisticsEnabled(false);
        topicConfig.setName(TOPIC_CONFIG_BLANK);
        topicConfig.addMessageListenerConfig(new ListenerConfig(this));
        config.addTopicConfig(topicConfig);
    }

    public void init(@NonNull HazelcastInstance hazelcastInstance) {
        memEntityResolvers.forEach(memEntityResolver -> memEntityResolver.initTopic(hazelcastInstance));
    }

    @SneakyThrows
    @Override
    public void onMessage(Message<ObjectNode> message) {
        var messageObject = message.getMessageObject();
        log.debug("Message received: {}", messageObject);
        memEntityResolvers.stream()
            .filter(resolver -> isMessageMatch(resolver, messageObject))
            .forEach(resolver -> resolver.onMessage(messageObject));
    }

    private boolean isMessageMatch(IMemEntityResolver resolver, ObjectNode messageObject) {
        return
            !AbstractMemEntityResolver.RESOLVER_UUID.equals(messageObject.get("memberUid").asText())
                && resolver.getName().equals(messageObject.get("nameMemEntity").asText());
    }
}
