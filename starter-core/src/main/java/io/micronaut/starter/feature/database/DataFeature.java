package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.OneOfFeature;

import java.util.LinkedHashMap;
import java.util.Map;

public interface DataFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return DataFeature.class;
    }

    default Map<String, Object> getDatasourceConfig() {
        Map<String, Object> conf = new LinkedHashMap<>();
        conf.put("datasources.default.schema-generate", "CREATE_DROP");
        conf.put("datasources.default.dialect", "H2");
        return conf;
    }
}
