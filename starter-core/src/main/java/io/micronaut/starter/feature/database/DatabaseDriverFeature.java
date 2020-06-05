/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

public abstract class DatabaseDriverFeature implements OneOfFeature {

    private final TestContainers testContainers;

    public DatabaseDriverFeature() {
        this.testContainers = null;
    }

    public DatabaseDriverFeature(TestContainers testContainers) {
        this.testContainers = testContainers;
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
        if (!featureContext.isPresent(TestContainers.class) && testContainers != null) {
            featureContext.addFeature(testContainers);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    abstract boolean embedded();

    abstract public String getJdbcUrl();

    abstract public String getDriverClass();

    abstract public String getDefaultUser();

    abstract public String getDefaultPassword();

    abstract public String getDataDialect();

}
