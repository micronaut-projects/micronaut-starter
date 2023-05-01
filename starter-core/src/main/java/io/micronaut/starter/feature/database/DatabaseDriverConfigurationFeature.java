/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.testresources.TestResources;

import java.util.Map;
import java.util.Optional;

import static io.micronaut.starter.feature.database.jdbc.JdbcFeature.PROPERTY_DATASOURCES_DEFAULT_DB_TYPE;

/**
 * A feature that configures a datasource with a driver
 */
public interface DatabaseDriverConfigurationFeature extends Feature {

    String getUrlKey();

    String getDriverKey();

    String getUsernameKey();

    String getPasswordKey();

    default void applyDefaultConfig(GeneratorContext generatorContext, DatabaseDriverFeature dbFeature, Map<String, Object> config) {
        if (!generatorContext.getFeatures().hasFeature(TestResources.class)) {
            Optional.ofNullable(dbFeature.getJdbcUrl()).ifPresent(url -> config.put(getUrlKey(), url));
            Optional.ofNullable(dbFeature.getDefaultUser()).ifPresent(user -> config.put(getUsernameKey(), user));
            Optional.ofNullable(dbFeature.getDefaultPassword()).ifPresent(pass -> config.put(getPasswordKey(), pass));
        }
        Optional.ofNullable(dbFeature.getDriverClass()).ifPresent(driver -> config.put(getDriverKey(), driver));
        dbFeature.getDbType().ifPresent(dbType -> config.put(PROPERTY_DATASOURCES_DEFAULT_DB_TYPE, dbType.toString()));
        final Map<String, Object> additionalConfig = dbFeature.getAdditionalConfig(generatorContext);
        if (!additionalConfig.isEmpty()) {
            config.putAll(additionalConfig);
        }
    }
}
