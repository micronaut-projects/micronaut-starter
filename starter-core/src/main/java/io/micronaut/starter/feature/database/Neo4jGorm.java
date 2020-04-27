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
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class Neo4jGorm implements Feature {

    private final Neo4jBolt neo4jBolt;

    public Neo4jGorm(Neo4jBolt neo4jBolt) {
        this.neo4jBolt = neo4jBolt;
    }

    @Override
    public String getName() {
        return "neo4j-gorm";
    }

    @Override
    public String getTitle() {
        return "GORM for Neo4j";
    }

    @Override
    public String getDescription() {
        return "Configures GORM for Neo4j for Groovy applications";
    }

    @Override
    public Optional<Language> getRequiredLanguage() {
        return Optional.of(Language.GROOVY);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Neo4jBolt.class)) {
            featureContext.addFeature(neo4jBolt);
        }
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
