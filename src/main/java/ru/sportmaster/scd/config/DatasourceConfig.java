package ru.sportmaster.scd.config;

import static java.util.Objects.nonNull;
import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_IMPLICIT_STATEMENT_CACHE_SIZE;
import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_TNS_ADMIN;
import static ru.sportmaster.scd.consts.ParamCredentials.DATABASE_CREDENTIAL_NAME;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.sportmaster.scd.credentials.ICredentials;
import ru.sportmaster.scd.exceptions.CredentialException;
import ru.sportmaster.scd.service.credentials.ICredentialsService;

@Configuration
@EnableEnversRepositories
@EnableTransactionManagement
@ConditionalOnClass({DataSource.class})
@RequiredArgsConstructor
public class DatasourceConfig {
    private static final String ORACLE_OCI_DRIVER_TYPE = "oci";
    private static final String ORACLE_NET_TNS_LANG = "oracle.net.tns_lang";
    private static final String ORACLE_CONNECTION_VALIDATION = "oracle.jdbc.defaultConnectionValidation";

    @Value("${spring.datasource.url:null}")
    private String url;
    @Value("${spring.datasource.hikari.maximum-pool-size:12}")
    private int maxPoolSize;
    @Value("${spring.datasource.hikari.idle-timeout:null}")
    private int idleTimeout;
    @Value("${spring.datasource.oracle.cache-statements:true}")
    private Boolean cacheStatements;
    @Value("${spring.datasource.oracle.cache-statements.size:250}")
    private String cacheStatementsSize;
    @Value("${spring.datasource.oracle.connection-validation:LOCAL}")
    private String connectionValidation;
    @Value("${spring.datasource.oracle.tns-name:}")
    private String tnsName;
    @Value("${spring.datasource.oracle.tns-admin:}")
    private String tnsAdmin;

    private final ICredentialsService credentialsService;

    /**
     * Oracle DataSource with Statement Cash into Hikari Pool.
     *
     * @return HikariDataSource
     * @throws SQLException SQLException
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "oracle.jdbc.OracleDriver")
    public DataSource dataSource() throws SQLException {
        final var databaseCredential =
            Optional.ofNullable(credentialsService.getCredential(DATABASE_CREDENTIAL_NAME))
                .map(ICredentials::getValue)
                .orElseThrow(() -> new CredentialException("Ошибка получения параметров подключения к БД"));
        System.setProperty("com.zaxxer.hikari.aliveBypassWindowMs", "-1");
        Properties dsProperties = new Properties();
        dsProperties.put(CONNECTION_PROPERTY_IMPLICIT_STATEMENT_CACHE_SIZE, cacheStatementsSize);
        dsProperties.put(ORACLE_NET_TNS_LANG, "AMERICAN_AMERICA.UTF8");
        dsProperties.put(ORACLE_CONNECTION_VALIDATION, connectionValidation);

        OracleDataSource oracleDatasource = new OracleDataSource();
        oracleDatasource.setImplicitCachingEnabled(cacheStatements);
        oracleDatasource.setUser(databaseCredential.getLogin());
        oracleDatasource.setConnectionProperties(dsProperties);
        oracleDatasource.setPassword(databaseCredential.getPassword());
        if (nonNull(tnsName) && !tnsName.isEmpty() && nonNull(tnsAdmin) && !tnsAdmin.isEmpty()) {
            System.setProperty(CONNECTION_PROPERTY_TNS_ADMIN, tnsAdmin);
            oracleDatasource.setTNSEntryName(tnsName);
            oracleDatasource.setDriverType(ORACLE_OCI_DRIVER_TYPE);
        } else {
            oracleDatasource.setURL(url);
        }

        HikariConfig config = new HikariConfig();
        config.setDataSource(oracleDatasource);
        config.setDataSourceProperties(dsProperties);
        config.setPoolName("Hikari_OracleStatementCachingPool");
        config.setConnectionTimeout(idleTimeout);
        config.setMaximumPoolSize(maxPoolSize);

        return new HikariDataSource(config);
    }
}
