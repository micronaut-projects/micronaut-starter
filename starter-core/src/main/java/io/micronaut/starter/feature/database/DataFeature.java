package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.OneOfFeature;

import java.util.LinkedHashMap;
import java.util.Map;

public interface DataFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return DataFeature.class;
    }

    default Map<String, Object> getDatasourceConfig() {
        final String prefix = "datasources.default.";
        Map<String, Object> conf = new LinkedHashMap<>();
        conf.put(prefix + "url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        conf.put(prefix + "driverClassName", "org.h2.Driver");
        conf.put(prefix + "username", "sa");
        conf.put(prefix + "password", "''");
        conf.put(prefix + "schema-generate", "CREATE_DROP");
        conf.put(prefix + "dialect", "H2");
        return conf;
    }
}
