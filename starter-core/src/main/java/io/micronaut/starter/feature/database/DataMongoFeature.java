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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

/**
 * Base class for our data-mongo features.
 */
public abstract class DataMongoFeature implements DataFeature {

    private static final String MONGODB_GROUP = "org.mongodb";
    private static final String MICRONAUT_DATA_GROUP = "io.micronaut.data";
    private static final String MICRONAUT_DATA_VERSION = "micronaut.data.version";
    private final Data data;

    protected DataMongoFeature(Data data) {
        this.data = data;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("mongodb.uri", "mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/mydb");

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

        // Needs to be an implementation dependency for the Groovy compiler
        if (generatorContext.getLanguage() == Language.GROOVY) {
            generatorContext.addDependency(Dependency.builder()
                    .compile()
                    .groupId(MONGODB_GROUP)
                    .artifactId(mongoArtifact()));
        } else {
            generatorContext.addDependency(Dependency.builder()
                    .runtime()
                    .groupId(MONGODB_GROUP)
                    .artifactId(mongoArtifact()));
        }
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.mongodb.com";
    }

    @NonNull
    protected abstract String mongoArtifact();
}
