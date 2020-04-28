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
import io.micronaut.starter.feature.database.H2;

import java.util.LinkedHashMap;

public abstract class JdbcFeature implements OneOfFeature {

    private final H2 h2;

    public JdbcFeature(H2 h2) {
        this.h2 = h2;
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
        if (!featureContext.isPresent(H2.class)) {
            featureContext.addFeature(h2);
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

}
