package io.micronaut.starter.feature.database;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationHelper {

    public static final Map<String, Object> JDBC_H2;

    static {
        final String prefix = "datasources.default.";
        JDBC_H2 = new LinkedHashMap<>();
        JDBC_H2.put(prefix + "url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        JDBC_H2.put(prefix + "driverClassName", "org.h2.Driver");
        JDBC_H2.put(prefix + "username", "sa");
        JDBC_H2.put(prefix + "password", "''");
    }
}
