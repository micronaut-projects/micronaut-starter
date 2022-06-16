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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.feature.testresources.TestResourcesFeature;

import java.util.Collections;
import java.util.Map;

public abstract class DatabaseDriverFeature extends TestResourcesFeature implements OneOfFeature {

    private final JdbcFeature jdbcFeature;

    public DatabaseDriverFeature() {
        this(null, null);
    }

    public DatabaseDriverFeature(JdbcFeature jdbcFeature, TestResources testResources) {
        super(testResources);
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public Class<?> getFeatureClass() {
        return DatabaseDriverFeature.class;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!(featureContext.isPresent(JdbcFeature.class) || featureContext.isPresent(R2dbcFeature.class))
            && jdbcFeature != null) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    public abstract boolean embedded();

    public String getJdbcUrl() {
        return null;
    }

    public String getR2dbcUrl() {
        return null;
    }

    public abstract String getDriverClass();

    public String getDefaultUser() {
        return null;
    }

    public String getDefaultPassword() {
        return null;
    }

    public abstract String getDataDialect();

    public Map<String, Object> getAdditionalConfig() {
        return Collections.emptyMap();
    }

}
