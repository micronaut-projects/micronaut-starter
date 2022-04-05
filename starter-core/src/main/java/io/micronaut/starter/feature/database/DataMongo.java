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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

/**
 * Add support for Micronaut Data MongoDB. 
 
 * @author graemerocher
 */
@Experimental
@Singleton
public class DataMongo implements DataFeature {

    private static final String MICRONAUT_DATA_GROUP = "io.micronaut.data";
    private static final String MICRONAUT_DATA_VERSION = "micronaut.data.version";

    private final MongoSync mongoSync;

    public DataMongo(MongoSync mongoSync) {
        this.mongoSync = mongoSync;
    }

    @Override
    public String getName() {
        return "data-mongodb";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(Dependency.builder()
                    .annotationProcessor()
                    .groupId(MICRONAUT_DATA_GROUP)
                    .artifactId("micronaut-data-processor")
                    .versionProperty(MICRONAUT_DATA_VERSION));
        }
        generatorContext.addDependency(Dependency.builder()
                .annotationProcessor()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId("micronaut-data-document-processor")
                .versionProperty(MICRONAUT_DATA_VERSION));
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId("micronaut-data-mongodb")
                .versionProperty(MICRONAUT_DATA_VERSION));
        if (generatorContext.isFeaturePresent(MongoSync.class) && generatorContext.isFeaturePresent(MongoReactive.class)) {
            generatorContext.getConfiguration().put("micronaut.data.mongodb.driver-type", "reactive");
        }
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(MongoReactive.class)) {
            featureContext.addFeature(mongoSync);
        }
    }

    @Override
    public String getDescription() {
        return "Adds support for defining data repositories for MongoDB";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data MongoDB";
    }
}
