package ru.sportmaster.scd.mementity;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.consts.ParamNames.SELECT_ALL_STATEMENT;
import static ru.sportmaster.scd.utils.JpaUtil.getFullTableName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.exceptions.ExecutingRuntimeException;
import ru.sportmaster.scd.repository.AbstractJdbcRepositoryImpl;

@Slf4j
public abstract class MemEntityRepositoryImpl<K, T>
    extends AbstractJdbcRepositoryImpl
    implements MemEntityRepository<K, T> {

    private final String name;
    private final int fetchSize;
    private final String selectStatement;
    private final Set<K> lastLoadKeys = new HashSet<>();

    protected MemEntityRepositoryImpl(String name,
                                      int fetchSize,
                                      Class<?> entityClass) {
        this.name = name;
        this.fetchSize = fetchSize;
        this.selectStatement = SELECT_ALL_STATEMENT + getFullTableName(entityClass);
    }

    @Override
    public void reloadMap(Map<K, T> map) {
        if (isNull(map)) {
            return;
        }
        lastLoadKeys.clear();
        log.debug("{} загрузка записей.", name);
        load(map);
        log.debug("{} загрузка {} записей завершена, удаление записей.", name, lastLoadKeys.size());
        var evicted = map.keySet().stream().filter(key -> !lastLoadKeys.contains(key)).toList();
        evicted.forEach(map::remove);
        log.debug("{} удаление {} записей завершено.", name, evicted.size());
        lastLoadKeys.clear();
    }

    private void load(@NonNull Map<K, T> map) {
        try (var con = getDataSource().getConnection()) {
            try (var stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                stmt.setFetchSize(fetchSize);
                try (var resultSet = stmt.executeQuery(selectStatement)) {
                    while (resultSet.next()) {
                        var obj = createFromResultSet(resultSet);
                        map.put(getKey(obj), obj);
                        lastLoadKeys.add(getKey(obj));
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new ExecutingRuntimeException(e);
        }
    }

    protected abstract T createFromResultSet(@NonNull ResultSet resultSet) throws SQLException;

    protected abstract K getKey(T obj);
}
