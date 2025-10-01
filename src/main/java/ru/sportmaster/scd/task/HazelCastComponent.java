package ru.sportmaster.scd.task;

import static com.hazelcast.core.LifecycleEvent.LifecycleState.MERGED;
import static com.hazelcast.core.LifecycleEvent.LifecycleState.SHUTTING_DOWN;
import static java.util.Objects.nonNull;

import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.config.Config;
import com.hazelcast.config.EntryListenerConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.FlakeIdGeneratorConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.ScheduledExecutorConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryLoadedListener;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.dto.settings.AlgSettingsStatus;
import ru.sportmaster.scd.dto.settings.AlgType;
import ru.sportmaster.scd.dto.tokens.VaultTokenMessage;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.mementity.MemEntityTopic;
import ru.sportmaster.scd.service.caches.HistoryCacheKey;
import ru.sportmaster.scd.service.task.TaskManager;
import ru.sportmaster.scd.tokens.VaultTokenMessageListener;
import ru.sportmaster.scd.ui.view.type.JpaViewSearchKey;

@Slf4j
@Component
public class HazelCastComponent {
    private static final String HISTORY_CACHE_NAME = "historyCache";
    private static final String QUEUE_NAME = "algorithmTaskQueue";
    private static final String GENERATOR_NAME = "algorithmQueueGenerator";
    private static final String QUEUE_LOCK_NAME = "algorithmQueueLock";
    private static final String CLUSTER_MEMBERS_COUNTER_NAME = "clusterMembersCounter";
    private static final String ALG_SETTINGS_LOCK_MAP = "algSettingsLockMap";
    private static final String SERVICE_TOPIC_NAME = "serviceClusterTopic";
    private static final String MERGE_CLUSTER_MESSAGE = "mergeClusterMessage";
    private static final String PIVOT_FILE_PREPARING_MAP = "pivotFilePreparing";
    private static final String VAULT_EXECUTOR_NAME = "vaultScheduledExec";
    private static final String VAULT_TOPIC_NAME = "vaultClusterTopic";
    private static final String VIEW_FILE_UPLOAD_VALIDATE_MAP = "viewFileUploadValidateMap";
    private static final String VIEW_SEARCH_POSITIONS_MAP = "viewSearchPositionsMap";

    @Getter
    private final HazelcastInstance hazelcastInstance;

    @Getter
    private final IMap<HistoryCacheKey, Object> historyCache;

    @Getter
    private final FlakeIdGenerator idGenerator;

    @Getter
    private IMap<AlgorithmTask, AlgorithmTaskState> queueTasks;

    @Getter
    private final Map<AlgType, AlgSettingsStatus> algSettingsLockMap;

    @Getter
    private final IMap<String, PivotPreparingFileDto> pivotFilePreparingMap;

    @Getter
    private final IMap<String, ViewValidateFileResponseDto> viewFileUploadValidateMap;

    @Getter
    private final IMap<JpaViewSearchKey, List<Integer>> viewSearchPositionsMap;

    @Getter
    private final FencedLock queueLock;
    private final IAtomicLong clusterMemberCounter;
    private long clusterMemberNumber;
    private final AtomicBoolean nonMergeCluster;
    private final ITopic<String> mergeClusterTopic;

    @Getter
    private final IScheduledExecutorService vaultScheduledExec;
    @Getter
    private final ITopic<VaultTokenMessage> vaultClusterTopic;

    @Setter
    private TaskManager taskManager;

    public HazelCastComponent(@Value("${scd.hazelcast.cluster-name}")
                              String clusterName,
                              @Value("${scd.hazelcast.member-list}")
                              String memberList,
                              @Value("${scd.file.stored-file-sec}")
                              Integer storedUploadFileSec,
                              @Value("${scd.history.cache.max-size:1000000}")
                              Integer cacheMaximumSize,
                              @Value("${scd.history.cache.minute-expire-after-access:1440}")
                              Integer minuteExpireAfterAccess,
                              QueueTaskStoreComponent queueTaskStoreComponent,
                              MemEntityTopic memEntityTopic) {

        var config = new Config();
        config.setInstanceName(clusterName);
        config.setClusterName(clusterName);

        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig()
            .setEnabled(false);
        var tcpIpConfig = join.getTcpIpConfig();
        for (var member : memberList.split(",")) {
            tcpIpConfig.addMember(member);
        }
        tcpIpConfig.setEnabled(true);

        var historyCacheEvictionConfig = new EvictionConfig();
        historyCacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        historyCacheEvictionConfig.setSize(cacheMaximumSize);
        historyCacheEvictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        var historyCacheConfig = config.getMapConfig(HISTORY_CACHE_NAME);
        historyCacheConfig.setTimeToLiveSeconds(minuteExpireAfterAccess * 60);
        historyCacheConfig.setMaxIdleSeconds(0);
        historyCacheConfig.setEvictionConfig(historyCacheEvictionConfig);

        config.addFlakeIdGeneratorConfig(new FlakeIdGeneratorConfig(GENERATOR_NAME));

        var queueConfig = config.getMapConfig(QUEUE_NAME);
        queueConfig.addEntryListenerConfig(
            new EntryListenerConfig(new QueueTaskListener(this), false, false)
        );
        var queueMapStoreConfig = new MapStoreConfig();
        queueMapStoreConfig.setEnabled(true);
        queueMapStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY);
        queueMapStoreConfig.setImplementation(queueTaskStoreComponent);
        queueConfig.setMapStoreConfig(queueMapStoreConfig);

        config.addListenerConfig(new ListenerConfig(new ClusterMembershipListener(this)));
        config.addListenerConfig(new ListenerConfig(new NodeLifecycleListener(this)));

        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setGlobalOrderingEnabled(false);
        topicConfig.setStatisticsEnabled(false);
        topicConfig.setName(SERVICE_TOPIC_NAME);
        topicConfig.addMessageListenerConfig(new ListenerConfig(new ServiceClusterMessageListener(this)));
        config.addTopicConfig(topicConfig);

        TopicConfig vaultTopicConfig = new TopicConfig();
        vaultTopicConfig.setGlobalOrderingEnabled(false);
        vaultTopicConfig.setStatisticsEnabled(false);
        vaultTopicConfig.setName(VAULT_TOPIC_NAME);
        vaultTopicConfig.addMessageListenerConfig(new ListenerConfig(new VaultTokenMessageListener()));
        config.addTopicConfig(vaultTopicConfig);

        memEntityTopic.setConfig(config);

        var vaultExecutorConfig = config.getScheduledExecutorConfig(VAULT_EXECUTOR_NAME);
        vaultExecutorConfig.setStatisticsEnabled(false);
        vaultExecutorConfig.setPoolSize(1);
        vaultExecutorConfig.setCapacity(1);
        vaultExecutorConfig.setCapacityPolicy(ScheduledExecutorConfig.CapacityPolicy.PER_NODE);

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        this.clusterMemberCounter =
            hazelcastInstance.getCPSubsystem().getAtomicLong(CLUSTER_MEMBERS_COUNTER_NAME);
        this.nonMergeCluster = new AtomicBoolean(true);

        this.historyCache = hazelcastInstance.getMap(HISTORY_CACHE_NAME);
        this.idGenerator = hazelcastInstance.getFlakeIdGenerator(GENERATOR_NAME);
        this.queueLock = hazelcastInstance.getCPSubsystem().getLock(QUEUE_LOCK_NAME);
        this.algSettingsLockMap = hazelcastInstance.getMap(ALG_SETTINGS_LOCK_MAP);
        this.mergeClusterTopic = hazelcastInstance.getTopic(SERVICE_TOPIC_NAME);

        this.pivotFilePreparingMap = hazelcastInstance.getMap(PIVOT_FILE_PREPARING_MAP);
        config.getMapConfig(PIVOT_FILE_PREPARING_MAP).setTimeToLiveSeconds(storedUploadFileSec);

        this.vaultScheduledExec = hazelcastInstance.getScheduledExecutorService(VAULT_EXECUTOR_NAME);
        this.vaultClusterTopic = hazelcastInstance.getTopic(VAULT_TOPIC_NAME);

        this.viewFileUploadValidateMap = hazelcastInstance.getMap(VIEW_FILE_UPLOAD_VALIDATE_MAP);
        config.getMapConfig(VIEW_FILE_UPLOAD_VALIDATE_MAP).setTimeToLiveSeconds(storedUploadFileSec);

        this.viewSearchPositionsMap = hazelcastInstance.getMap(VIEW_SEARCH_POSITIONS_MAP);
    }

    public void init() {
        clusterMemberNumber = clusterMemberCounter.incrementAndGet();
        queueTasks = hazelcastInstance.getMap(QUEUE_NAME);
    }

    public boolean isFirstClusterMember() {
        return clusterMemberNumber == 1;
    }

    public boolean nonMergeCluster() {
        return nonMergeCluster.get();
    }

    private void mergeCluster() {
        nonMergeCluster.set(false);
    }

    private void publishAndMergeCluster() {
        mergeCluster();
        mergeClusterTopic.publish(MERGE_CLUSTER_MESSAGE);
    }

    private void startTasks() {
        if (nonNull(taskManager)) {
            taskManager.getAndStartTasks();
        }
    }

    private void scheduleStartTasks() {
        if (nonNull(taskManager)) {
            taskManager.getScheduleGetAndStartTasks().run();
        }
    }

    private void preDestroy() {
        clusterMemberCounter.decrementAndGet();
        if (nonNull(taskManager)) {
            taskManager.preDestroy();
        } else {
            log.debug("TaskManager is null.");
        }
    }

    private record ClusterMembershipListener(HazelCastComponent hazelCastComponent) implements MembershipListener {
        @Override
        public void memberAdded(MembershipEvent var1) {
            log.debug("Member added: {}", var1.getMember().getAddress());
        }

        @Override
        public void memberRemoved(MembershipEvent var1) {
            log.debug("Member removed: {}", var1.getMember().getAddress());
            hazelCastComponent.startTasks();
        }
    }

    private record NodeLifecycleListener(HazelCastComponent hazelCastComponent) implements LifecycleListener {
        @Override
        public void stateChanged(LifecycleEvent var1) {
            log.debug("Cluster node state: {}", var1.getState());
            if (SHUTTING_DOWN.equals(var1.getState())) {
                hazelCastComponent.preDestroy();
            } else {
                if (MERGED.equals(var1.getState())) {
                    hazelCastComponent.publishAndMergeCluster();
                }
            }
        }
    }

    private record QueueTaskListener(HazelCastComponent hazelCastComponent) implements
        EntryAddedListener<AlgorithmTask, AlgorithmTaskState>,
        EntryLoadedListener<AlgorithmTask, AlgorithmTaskState> {

        @Override
        public void entryAdded(EntryEvent<AlgorithmTask, AlgorithmTaskState> event) {
            hazelCastComponent.startTasks();
            log.debug("Queue entry added: {}", event);
        }

        @Override
        public void entryLoaded(EntryEvent<AlgorithmTask, AlgorithmTaskState> event) {
            hazelCastComponent.startTasks();
            log.debug("Queue entry loaded: {}", event);
        }

    }

    private record ServiceClusterMessageListener(HazelCastComponent hazelCastComponent) implements
        MessageListener<String> {
        @Override
        public void onMessage(Message<String> message) {
            var valueMessage = message.getMessageObject();
            log.debug("Message received: {}", valueMessage);
            if (Objects.equals(valueMessage, MERGE_CLUSTER_MESSAGE)) {
                hazelCastComponent.mergeCluster();
                hazelCastComponent.scheduleStartTasks();
            }
        }
    }
}
