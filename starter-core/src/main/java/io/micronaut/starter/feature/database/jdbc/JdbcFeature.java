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
package io.micronaut.starter.feature.database.jdbc;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;

import java.util.LinkedHashMap;

public abstract class JdbcFeature implements OneOfFeature {

    private final DatabaseDriverFeature defaultDbFeature;

    public JdbcFeature(DatabaseDriverFeature defaultDbFeature) {
        this.defaultDbFeature = defaultDbFeature;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOW.getOrder();
    }

    @Override
    public Class<?> getFeatureClass() {
        return JdbcFeature.class;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("datasources.default", new LinkedHashMap<>());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc";
    }

}
