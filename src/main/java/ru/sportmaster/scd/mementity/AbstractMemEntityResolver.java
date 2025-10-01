package ru.sportmaster.scd.mementity;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.consts.ParamNames.PREFIX_MEM_ENTITY_TOPIC_NAME;
import static ru.sportmaster.scd.utils.UiUtil.isDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.mementity.MemEntitySelectDto;
import ru.sportmaster.scd.dto.view.SelectionCommand;
import ru.sportmaster.scd.dto.view.SelectionRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchResponseDto;
import ru.sportmaster.scd.exceptions.SearchNotInitializedException;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UIViewMemSelection;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;
import ru.sportmaster.scd.utils.UiUtil;

@Slf4j
public abstract class AbstractMemEntityResolver<K, T> implements IMemEntityResolver {
    public static final String RESOLVER_UUID = UUID.randomUUID().toString();

    @Getter
    private final String name;
    private final IMemEntity<K, T> memEntity;
    private final ObjectMapper objectMapper;

    private final Cache<String, List<K>> keysMap;
    private final Cache<String, MemEntityFetchParams> paramsMap;
    private final IMemEntityConditionBuilder<T> conditionBuilder = new MemEntityConditionBuilder<>();
    private final IMemEntityCheckProcessor<K, T> checkProcessor;
    private final Map<String, List<Integer>> searchMap = new HashMap<>();

    private ITopic<ObjectNode> resolverTopic;

    protected AbstractMemEntityResolver(String name,
                                        IMemEntity<K, T> memEntity,
                                        ObjectMapper objectMapper,
                                        IMemEntityCheckProcessor<K, T> checkProcessor,
                                        long expireHours) {
        this.name = name;
        this.memEntity = memEntity;
        this.objectMapper = objectMapper;

        this.keysMap =
            Caffeine.newBuilder()
                .expireAfterWrite(expireHours, TimeUnit.HOURS)
                .build();
        this.paramsMap =
            Caffeine.newBuilder()
                .expireAfterWrite(expireHours, TimeUnit.HOURS)
                .build();

        this.checkProcessor = checkProcessor;
    }

    @Override
    public Page<ObjectNode> findAll(UiViewFetchRequest request,
                                    String sessionUuid,
                                    String formUuid) {
        log.debug("{} запрос данных для {}.", name, formUuid);
        if (isNull(request) || isNull(formUuid)) {
            return new PageImpl<>(Collections.emptyList());
        }
        var params = MemEntityFetchParams.createFromRequest(request);
        createListAsNeed(params, sessionUuid, formUuid);
        return getResultList(request, sessionUuid, formUuid);
    }



    @Override
    public Stream<?> findAllStream(UiViewFetchRequest request, String sessionUuid, String formUuid) {
        log.debug("{} запрос стрима для {}.", name, formUuid);
        if (isNull(request) || isNull(formUuid)) {
            return Stream.empty();
        }
        var params = MemEntityFetchParams.createFromRequest(request);
        createListAsNeed(params, sessionUuid, formUuid);
        return getResultStream(request, formUuid);
    }

    @Override
    public List<?> findFieldValuesByPath(UIViewFetchFieldValuesRequest request,
                                         String sessionUuid,
                                         String formUuid) {
        log.debug("{} запрос значений поля для {}.", name, formUuid);
        if (isNull(request) || isNull(formUuid)) {
            return Collections.emptyList();
        }
        var params = MemEntityFetchParams.createFromRequest(request);
        createListAsNeed(params, sessionUuid, formUuid);
        return getResultFieldList(request, formUuid);
    }

    private void createListAsNeed(@NonNull MemEntityFetchParams params,
                                  @NonNull String sessionUuid,
                                  @NonNull String formUuid) {
        createListAsNeed(params, sessionUuid, formUuid, true);
    }

    private void createListAsNeed(@NonNull MemEntityFetchParams params,
                                  @NonNull String sessionUuid,
                                  @NonNull String formUuid,
                                  boolean isNeedSendMessage) {
        executeSelectRestore(sessionUuid, formUuid, isNeedSendMessage);
        if (isPresentAndNonChangeRequest(params, formUuid)) {
            return;
        }
        createList(params, sessionUuid, formUuid, isNeedSendMessage);
    }

    private boolean isPresentAndNonChangeRequest(@NonNull MemEntityFetchParams params,
                                                 @NonNull String formUuid) {
        return
            nonNull(keysMap.getIfPresent(formUuid))
                && Optional.ofNullable(paramsMap.getIfPresent(formUuid))
                .map(existParams -> existParams.equals(params))
                .orElse(false);
    }

    private void executeSelectRestore(@NonNull String sessionUuid,
                                      @NonNull String formUuid,
                                      boolean isNeedSendMessage) {
        if (checkProcessor.nonRestoreNeeded(sessionUuid)) {
            return;
        }
        var request = SelectionRequestDto.createCommand(SelectionCommand.RESTORE);
        if (isNeedSendMessage) {
            exec(sessionUuid, formUuid, request);
        } else {
            checkProcessor.exec(sessionUuid, formUuid, request, this::getKeysForCheck);
        }
    }

    private void createList(@NonNull MemEntityFetchParams params,
                            @NonNull String sessionUuid,
                            @NonNull String formUuid,
                            boolean isNeedSendMessage) {
        log.debug("{} начато создание списка ключей для {}.", name, formUuid);
        paramsMap.put(formUuid, params);
        keysMap.put(
            formUuid,
            getKeys(params, formUuid)
        );
        if (isNeedSendMessage) {
            sendFetchTopicMessage(params, sessionUuid, formUuid);
        }
        log.debug("{} окончено создание списка ключей для {}.", name, formUuid);
    }

    private List<K> getKeys(@NonNull MemEntityFetchParams params, String formUuid) {
        var predicate = getPredicate(params.getConditions());
        var sortedOrder = conditionBuilder.buildOrder(params.getSort());
        var selectionPredicate = getMemSelectionPredicate(formUuid, params.getMemSelections());

        return
            memEntity.getMap()
                .values()
                .parallelStream()
                .filter(predicate)
                .sorted(sortedOrder)
                .map(this::getKey)
                .filter(selectionPredicate)
                .toList();
    }

    private List<Object> getKeysForCheck(List<ICondition> conditions) {
        return memEntity.getMap()
            .values()
            .parallelStream()
            .filter(getPredicate(conditions))
            .map(this::getKey)
            .map(i -> (Object) i)
            .toList();
    }

    private Predicate<K> getMemSelectionPredicate(String formUuid, List<UIViewMemSelection> selections) {
        if (isNull(selections) || selections.isEmpty()) {
            return k -> true;
        }

        UIViewMemSelection selectionRequest = selections.get(0);
        boolean selectSelf = memEntity.getName().equals(selectionRequest.getView());
        if (selections.size() > 1 || !selectSelf || !ID.equals(selectionRequest.getField())) {
            return k -> true;
        }

        MemEntitySelection<K> selection = checkProcessor.getMemEntitySelection(formUuid);
        return selection::isChecked;
    }

    private Predicate<T> getPredicate(List<ICondition> conditions) {
        if (isNull(conditions) || conditions.isEmpty()) {
            return o -> true;
        }
        return conditionBuilder.buildPredicates(conditions);
    }

    protected abstract K getKey(@NonNull T o);

    private Page<ObjectNode> getResultList(@NonNull UiViewFetchRequest request,
                                           @NonNull String sessionUuid,
                                           @NonNull String formUuid) {
        var dataKeysList = keysMap.getIfPresent(formUuid);
        if (isNull(dataKeysList)) {
            return new PageImpl<>(Collections.emptyList());
        }

        var startPos = (int) Math.min(request.getOffset(), dataKeysList.size());
        var endPos = Math.min(startPos + request.getSize(), dataKeysList.size());

        return
            new PageImpl<>(
                dataKeysList
                    .subList(startPos, endPos)
                    .parallelStream()
                    .map(key -> checkProcessor.process(sessionUuid, key, memEntity.getMap().get(key)))
                    .map(entityDto -> objectMapper.convertValue(entityDto, ObjectNode.class))
                    .toList(),
                Pageable.unpaged(),
                dataKeysList.size()
            );
    }

    private Stream<?> getResultStream(@NonNull UiViewFetchRequest request,
                                      @NonNull String formUuid) {
        var dataKeysList = keysMap.getIfPresent(formUuid);
        if (isNull(dataKeysList)) {
            return Stream.empty();
        }

        var startPos = (int) Math.min(request.getOffset(), dataKeysList.size());
        var endPos = Math.min(startPos + request.getSize(), dataKeysList.size());

        return dataKeysList.subList(startPos, endPos).parallelStream();
    }

    private List<?> getResultFieldList(@NonNull UIViewFetchFieldValuesRequest request,
                                       @NonNull String formUuid) {
        var dataKeysList = keysMap.getIfPresent(formUuid);
        if (isNull(dataKeysList)) {
            return Collections.emptyList();
        }

        return dataKeysList
            .subList(0, dataKeysList.size())
            .parallelStream()
            .map(key -> getFieldValue(memEntity.getMap().get(key), request.getPath()))
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    }

    private void sendFetchTopicMessage(@NonNull MemEntityFetchParams params,
                                       @NonNull String sessionUuid,
                                       @NonNull String formUuid) {
        log.debug("{} отправка сообщения FETCH.", name);
        var message =
            objectMapper.convertValue(
                MemEntityFetchTopicMessage.builder()
                    .memberUid(RESOLVER_UUID)
                    .nameMemEntity(name)
                    .typeTopic(MemEntityTypeTopicMessage.FETCH)
                    .params(params)
                    .sessionUuid(sessionUuid)
                    .formUuid(formUuid)
                    .build(),
                ObjectNode.class);
        resolverTopic.publish(message);
    }

    @Override
    public void onMessage(ObjectNode message) {
        log.debug("{} принято сообщение.", name);
        var messageObj = objectMapper.convertValue(message, MemEntityTopicMessage.class);
        switch (messageObj.getTypeTopic()) {
            case FETCH -> onFetchMessage(message);
            case SELECT -> onSelectMessage(message);
            case SEARCH -> onSearchMessage(message);
            case CLEAR_SEARCH -> onClearSearchMessage(message);
            case CLEAR -> onClearMessage(message);
            default -> log.debug("{} неизвестный тип сообщения {}", name, messageObj.getTypeTopic());
        }
    }

    private void onFetchMessage(ObjectNode message) {
        log.debug("{} обработка FETCH сообщения.", name);
        var messageObj = objectMapper.convertValue(message, MemEntityFetchTopicMessage.class);
        createListAsNeed(messageObj.getParams(), messageObj.getSessionUuid(), messageObj.getFormUuid(), false);
    }

    private void onSelectMessage(ObjectNode message) {
        log.debug("{} обработка SELECT сообщения.", name);
        var messageObj = objectMapper.convertValue(message, MemEntitySelectTopicMessage.class);
        checkProcessor.exec(messageObj.getSessionUuid(), messageObj.getFormUuid(),
            messageObj.getRequest(), this::getKeysForCheck);
    }

    private void onSearchMessage(ObjectNode message) {
        log.debug("{} обработка SEARCH сообщения.", name);
        var messageObj = objectMapper.convertValue(message, MemEntitySearchTopicMessage.class);
        pageSearch(messageObj.getRequest(), false);
    }

    private void onClearSearchMessage(ObjectNode message) {
        log.debug("{} обработка CLEAR_SEARCH сообщения.", name);
        var messageObj = objectMapper.convertValue(message, MemEntityTopicMessage.class);
        clearSearch(messageObj.getSessionUuid(), false);
    }

    private void onClearMessage(ObjectNode message) {
        log.debug("{} обработка CLEAR сообщения.", name);
        var messageObj = objectMapper.convertValue(message, MemEntityTopicMessage.class);
        clear(messageObj.getFormUuid(), false);
    }

    @Override
    public void initTopic(@NonNull HazelcastInstance hazelcastInstance) {
        log.debug("{} инициализация Topic", name);
        resolverTopic = hazelcastInstance.getTopic(PREFIX_MEM_ENTITY_TOPIC_NAME + name);
    }

    @Override
    public Page<ObjectNode> findAllOnlyFields(UIViewFetchFieldRequest request) {
        throw new NotImplementedException("findAllOnlyFields(request) not implemented");
    }

    @Override
    public Page<Object> findAllIds(UiViewFetchRequest request) {
        throw new NotImplementedException("findAllIds(request) not implemented");
    }

    @Override
    public Object findOne(UiViewFetchRequest request) {
        throw new NotImplementedException("findOne(request) not implemented");
    }

    @Override
    public void exec(String sessionUuid, String formUuid, SelectionRequestDto request) {
        var response = checkProcessor.exec(sessionUuid, formUuid, request, this::getKeysForCheck);
        if (nonNull(response)) {
            sendSelectTopicMessage(response, sessionUuid, formUuid);
        }
    }

    @Override
    public MemEntitySelectDto<K, T> getSelectionInfo(String sessionUuid, String formUuid) {
        MemEntitySelectDto<K, T> select = checkProcessor.get(sessionUuid, formUuid, keysMap.getIfPresent(formUuid));
        if (nonNull(select.getFirstValueKey())) {
            select.setFirstValue(memEntity.getMap().get(select.getFirstValueKey()));
        }
        return select;
    }

    private Object getFieldValue(Object entity, String field) {
        if (entity instanceof IMemEntityGetter getter) {
            Object value = getter.getFieldValue(field);
            if (nonNull(value) && isDate(value)) {
                return LocalDate.from((TemporalAccessor) value).format(UiUtil.DATE_FORMAT);
            }
            return value;
        } else {
            return null;
        }
    }

    @Override
    public void clear(String formUuid) {
        clear(formUuid, true);
    }

    private void clear(String formUuid,
                       boolean isNeedSendMessage) {
        paramsMap.invalidate(formUuid);
        keysMap.invalidate(formUuid);
        checkProcessor.clear(formUuid);
        if (isNeedSendMessage) {
            sendClearTopicMessage(formUuid);
        }
    }

    @Override
    public UiViewSearchResponseDto pageSearch(UiViewSearchRequestDto request) {
        return pageSearch(request, true);
    }

    private UiViewSearchResponseDto pageSearch(UiViewSearchRequestDto request,
                                               boolean isNeedSendMessage) {
        if (isNull(request) || isNull(request.getSearchText()) || request.getSearchText().trim().isEmpty()) {
            return UiViewSearchResponseDto.builder().build();
        }
        var keys = keysMap.getIfPresent(request.getFormUuid());
        if (isNull(keys) || keys.isEmpty() || request.getOffset() >= keys.size()) {
            return UiViewSearchResponseDto.builder().build();
        }
        var valuesMap = memEntity.getMap();
        if (isNull(valuesMap) || keys.isEmpty()) {
            return UiViewSearchResponseDto.builder().build();
        }
        Integer firstIndex = null;
        int lastIndex = request.getOffset() + request.getSize();
        if (lastIndex > keys.size()) {
            lastIndex = keys.size();
        }
        var matchedIndexes =
            IntStream.range(request.getOffset(), lastIndex)
                .filter(i -> isSearchMatch(i, keys, valuesMap, request.getSearchText()))
                .boxed()
                .toList();
        Optional.ofNullable(getSearchList(request))
            .ifPresentOrElse(
                list -> list.addAll(matchedIndexes),
                () -> {
                    throw new SearchNotInitializedException(request);
                }
            );
        if (!matchedIndexes.isEmpty()) {
            firstIndex = matchedIndexes.get(0);
        }
        if (isNeedSendMessage) {
            sendSearchTopicMessage(request, request.getSessionUuid(), request.getFormUuid());
        }
        return UiViewSearchResponseDto.builder()
            .firstIndex(firstIndex)
            .totalInPage(matchedIndexes.size())
            .matches(matchedIndexes)
            .build();
    }

    private List<Integer> getSearchList(@NonNull UiViewSearchRequestDto request) {
        List<Integer> result;
        if (request.isNewSearch()) {
            result = new ArrayList<>();
            searchMap.put(request.getSessionUuid(), result);
        } else {
            result = searchMap.get(request.getSessionUuid());
        }
        return result;
    }

    private boolean isSearchMatch(int index,
                                  @NonNull List<K> keys,
                                  @NonNull Map<K, T> valuesMap,
                                  @NonNull String searchText) {
        return
            Optional.ofNullable(keys.get(index))
                .map(valuesMap::get)
                .map(obj -> isSearchMatch(obj, searchText))
                .orElse(false);
    }

    protected abstract boolean isSearchMatch(T obj,
                                             @NonNull String searchText);

    @Override
    public int getSearchItemPosition(String sessionUuid,
                                     int currentPosition,
                                     Byte direction) {
        var searchList = searchMap.get(sessionUuid);
        if (isNull(searchList) || searchList.isEmpty()) {
            return 0;
        }
        var newPosition = currentPosition + direction;
        if (newPosition < 0 || newPosition > searchList.size()) {
            return 0;
        }
        return
            searchList.get(newPosition);
    }

    @Override
    public void clearSearch(String sessionUuid) {
        clearSearch(sessionUuid, true);
    }

    private void clearSearch(String sessionUuid,
                             boolean isNeedSendMessage) {
        searchMap.remove(sessionUuid);
        if (isNeedSendMessage) {
            sendClearSearchTopicMessage(sessionUuid);
        }
    }

    private void sendSelectTopicMessage(@NonNull SelectionRequestDto request,
                                        @NonNull String sessionUuid,
                                        @NonNull String formUuid) {
        log.debug("{} отправка сообщения SELECT.", name);
        var message =
            objectMapper.convertValue(
                MemEntitySelectTopicMessage.builder()
                    .memberUid(RESOLVER_UUID)
                    .nameMemEntity(name)
                    .typeTopic(MemEntityTypeTopicMessage.SELECT)
                    .request(request)
                    .sessionUuid(sessionUuid)
                    .formUuid(formUuid)
                    .build(),
                ObjectNode.class);
        resolverTopic.publish(message);
    }

    private void sendSearchTopicMessage(@NonNull UiViewSearchRequestDto request,
                                        @NonNull String sessionUuid,
                                        @NonNull String formUuid) {
        log.debug("{} отправка сообщения SEARCH.", name);
        var message =
            objectMapper.convertValue(
                MemEntitySearchTopicMessage.builder()
                    .memberUid(RESOLVER_UUID)
                    .nameMemEntity(name)
                    .typeTopic(MemEntityTypeTopicMessage.SEARCH)
                    .request(request)
                    .sessionUuid(sessionUuid)
                    .formUuid(formUuid)
                    .build(),
                ObjectNode.class);
        resolverTopic.publish(message);
    }

    private void sendClearSearchTopicMessage(@NonNull String sessionUuid) {
        log.debug("{} отправка сообщения CLEAR_SEARCH.", name);
        var message =
            objectMapper.convertValue(
                MemEntityTopicMessage.buildSuper()
                    .memberUid(RESOLVER_UUID)
                    .nameMemEntity(name)
                    .typeTopic(MemEntityTypeTopicMessage.CLEAR_SEARCH)
                    .sessionUuid(sessionUuid)
                    .formUuid(null)
                    .build(),
                ObjectNode.class);
        resolverTopic.publish(message);
    }

    private void sendClearTopicMessage(@NonNull String formUUID) {
        log.debug("{} отправка сообщения CLEAR.", name);
        var message =
            objectMapper.convertValue(
                MemEntityTopicMessage.buildSuper()
                    .memberUid(RESOLVER_UUID)
                    .nameMemEntity(name)
                    .typeTopic(MemEntityTypeTopicMessage.CLEAR)
                    .sessionUuid(null)
                    .formUuid(formUUID)
                    .build(),
                ObjectNode.class);
        resolverTopic.publish(message);
    }
}
