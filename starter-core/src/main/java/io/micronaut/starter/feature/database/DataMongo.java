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

import io.micronaut.core.annotation.Experimental;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

/**
 * Add support for Micronaut Data MongoDB. 
 
 * @author graemerocher
 */
@Experimental
@Singleton
public class DataMongo implements Feature {

    // TODO: Need to support either sync or async
    private final MongoSync mongoSync;

    public DataMongo(MongoSync mongoSync) {
        this.mongoSync = mongoSync;
    }

    @Override
    public String getName() {
        return "data-mongodb";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/3.3.x/guide/#mongo";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(
            Dependency.builder()
                .annotationProcessor()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-document-processor")
                .version("3.3.0-M1")
        );
        generatorContext.addDependency(
            Dependency.builder()
                .compile()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-mongodb")
                .version("3.3.0-M1")
        );        
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(mongoSync);
    }

    @Override
    public String getDescription() {
        return "Adds support for defining data repositories for MongoDB";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data MongoDB";
    }

    @Override
    public boolean isPreview() {
        return true;
    }
}
