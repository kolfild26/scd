package ru.sportmaster.scd.utils;

import static ru.sportmaster.scd.consts.ParamNames.STRING_ARRAY_TYPE_NAME;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import oracle.jdbc.OracleConnection;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StructUtil {
    public static OracleConnection getOracleConnection(DataSource dataSource) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
        oracleConnection.setAutoCommit(false);
        return oracleConnection;
    }

    public static Array stringListToArray(List<String> sourceList,
                                          Connection connection) throws SQLException {
        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }
        return
            ((OracleConnection) connection)
                .createOracleArray(
                    STRING_ARRAY_TYPE_NAME,
                    sourceList.stream()
                        .filter(Objects::nonNull)
                        .toArray()
                );

    }
}
