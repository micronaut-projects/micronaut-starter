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
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

/**
 * Base class for our data-mongo features.
 */
public abstract class DataMongoFeature extends EaseTestingFeature implements DataFeature {

    private static final String MONGODB_GROUP = "org.mongodb";
    private static final String MICRONAUT_DATA_GROUP = "io.micronaut.data";
    private static final String MICRONAUT_DATA_VERSION = "micronaut.data.version";
    private static final String MICRONAUT_DATA_PROCESSOR_ARTIFACT = "micronaut-data-processor";
    private static final String MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT = "micronaut-data-document-processor";
    private static final String MICRONAUT_DATA_MONGODB_ARTIFACT = "micronaut-data-mongodb";
    private static final String MONGODB_URI_CONFIGURATION_KEY = "mongodb.uri";
    private static final String MONGODB_URI_CONFIGURATION_VALUE = "mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/mydb";

    private final Data data;

    protected DataMongoFeature(Data data, TestContainers testContainers, TestResources testResources) {
        super(testContainers, testResources);
        this.data = data;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!generatorContext.getFeatures().isFeaturePresent(TestResources.class)) {
            generatorContext.getConfiguration().put(MONGODB_URI_CONFIGURATION_KEY, MONGODB_URI_CONFIGURATION_VALUE);
        }

        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(Dependency.builder()
                    .order(Priority.MICRONAUT_DATA_PROCESSOR.getOrder())
                    .annotationProcessor(true)
                    .groupId(MICRONAUT_DATA_GROUP)
                    .artifactId(MICRONAUT_DATA_PROCESSOR_ARTIFACT)
                    .versionProperty(MICRONAUT_DATA_VERSION));
        }
        generatorContext.addDependency(Dependency.builder()
                .annotationProcessor()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId(MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT)
                .versionProperty(MICRONAUT_DATA_VERSION));
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId(MICRONAUT_DATA_MONGODB_ARTIFACT)
                .versionProperty(MICRONAUT_DATA_VERSION));

        Dependency.Builder driverDependency = Dependency.builder()
                .groupId(MONGODB_GROUP)
                .artifactId(mongoArtifact());
        // Needs to be an implementation dependency for the Groovy compiler
        driverDependency = generatorContext.getLanguage() == Language.GROOVY ? driverDependency.compile() : driverDependency.runtime();
        generatorContext.addDependency(driverDependency);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeature(data);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.mongodb.com";
    }

    @NonNull
    protected abstract String mongoArtifact();
}
