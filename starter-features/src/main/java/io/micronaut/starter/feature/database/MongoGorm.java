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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.GroovySpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class MongoGorm implements GroovySpecificFeature {

    private final MongoReactive mongoReactive;

    public MongoGorm(MongoReactive mongoReactive) {
        this.mongoReactive = mongoReactive;
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public String getName() {
        return "mongo-gorm";
    }

    @Override
    public String getTitle() {
        return "GORM for MongoDB";
    }

    @Override
    public String getDescription() {
        return "Configures GORM for MongoDB for Groovy applications";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(MongoReactive.class)) {
            featureContext.addFeature(mongoReactive);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.groovy")
                .artifactId("micronaut-mongo-gorm")
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://gorm.grails.org/latest/mongodb/manual/";
    }
}
