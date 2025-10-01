package ru.sportmaster.scd.mementity;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.utils.JpaUtil.getIdFieldTableName;
import static ru.sportmaster.scd.utils.JpaUtil.getTableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.exceptions.ExecutingRuntimeException;
import ru.sportmaster.scd.repository.AbstractJdbcRepositoryImpl;

@Slf4j
public abstract class MemEntityCheckProcessorRepositoryImpl<K>
    extends AbstractJdbcRepositoryImpl
    implements MemEntityCheckProcessorRepository<K> {
    private static final String NUM_TRUNCATE_STATEMENT = "truncate table ascd_fs_sel_num_gtt";
    private static final String NUM_INSERT_STATEMENT = "insert into ascd_fs_sel_num_gtt(sel_value) values (?)";
    private static final String NUM_SAVE_CALL_STATEMENT = "call ascd_p_fs_api.save_number_filter(?, ?, ?, ?, ?)";
    private static final String CLOSE_CALL_STATEMENT = "call ascd_p_fs_api.close_filter(?, ?)";

    private final String tableName;
    private final String keyFieldName;

    protected MemEntityCheckProcessorRepositoryImpl(Class<?> entityClass) {
        this.tableName = getTableName(entityClass);
        this.keyFieldName = getIdFieldTableName(entityClass);
    }

    @Override
    @Transactional
    public void saveNum(@NonNull String formUuid,
                        Long userId,
                        MemEntitySelection<K> selection) {
        log.debug("{} сохранение фильтра.", formUuid);
        if (isNull(selection)) {
            return;
        }
        try (var connection = getDataSource().getConnection()) {
            log.debug("{} очистка временной таблицы.", formUuid);
            clearNumTemporary(connection);
            log.debug("{} заполнение временной таблицы.", formUuid);
            saveNumChoice(connection, selection);
            log.debug("{} сохранение данных фильтра.", formUuid);
            executeNumSave(connection, formUuid, userId, selection);
            log.debug("{} сохранение фильтра закончено.", formUuid);
        } catch (SQLException e) {
            throw new ExecutingRuntimeException(e);
        }
    }

    private void clearNumTemporary(@NonNull Connection connection) throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute(NUM_TRUNCATE_STATEMENT);
        }
    }

    private void saveNumChoice(@NonNull Connection connection,
                               @NonNull MemEntitySelection<K> selection) throws SQLException {
        try (var stmt = connection.prepareStatement(NUM_INSERT_STATEMENT)) {
            stmt.clearBatch();
            saveNumChoices(stmt, selection.getChoices());
            stmt.executeBatch();
        }
    }

    private void saveNumChoices(@NonNull PreparedStatement stmt,
                                @NonNull Set<K> choices) {
        int index = 1;
        for (K key : choices) {
            addNumBatch(stmt, key, index);
            index = index + 1;
        }
    }

    private void addNumBatch(@NonNull PreparedStatement stmt,
                             K key,
                             int index) {
        try {
            stmt.setLong(1, getLongKey(key));
            stmt.addBatch();
            if (index % 1000 == 0) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new ExecutingRuntimeException(e);
        }
    }

    protected abstract Long getLongKey(K key);

    private void executeNumSave(@NonNull Connection connection,
                                @NonNull String formUuid,
                                Long userId,
                                @NonNull MemEntitySelection<K> selection) throws SQLException {
        try (var stmt = connection.prepareCall(NUM_SAVE_CALL_STATEMENT)) {
            stmt.setString(1, formUuid);
            stmt.setString(2, tableName);
            stmt.setString(3, keyFieldName);
            stmt.setInt(4, selection.getTypeSelection().ordinal());
            stmt.setLong(5, userId);
            stmt.execute();
        }
    }

    @Override
    @Transactional
    public void close(@NonNull String formUuid) {
        try (var connection = getDataSource().getConnection()) {
            executeClose(connection, formUuid);
        } catch (SQLException e) {
            throw new ExecutingRuntimeException(e);
        }
    }

    private void executeClose(@NonNull Connection connection,
                              @NonNull String formUuid) throws SQLException {
        try (var stmt = connection.prepareCall(CLOSE_CALL_STATEMENT)) {
            stmt.setString(1, formUuid);
            stmt.setString(2, tableName);
            stmt.execute();
        }
    }
}
