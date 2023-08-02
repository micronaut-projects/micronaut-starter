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
package io.micronaut.starter.feature.camunda;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.server.Jetty;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.starter.feature.test.AssertJ;
import jakarta.inject.Singleton;

@Singleton
public class Platform7 implements CamundaCommunityFeature {
    public static final String NAME = "camunda-platform7";

    private static final Dependency.Builder DEPENDENCY_PLATFORM7 = Dependency.builder()
            .lookupArtifactId("micronaut-camunda-bpm-feature")
            .compile();

    private static final Dependency.Builder DEPENDENCY_BPM_ASSERT = Dependency.builder()
            .lookupArtifactId("camunda-bpm-assert")
            .test();

    private final DatabaseDriverFeature defaultDbFeature;
    private final Jetty jetty;
    private final AssertJ assertJ;

    public Platform7(DatabaseDriverFeature defaultDbFeature, Jetty jetty, AssertJ assertJ) {
        this.defaultDbFeature = defaultDbFeature;
        this.jetty = jetty;
        this.assertJ = assertJ;
    }

    @NonNull
    @Override
    public String getCommunityFeatureName() {
        return "platform7";
    }

    @Override
    @NonNull
    public String getCommunityFeatureTitle() {
        return "Camunda Platform 7 Workflow Engine";
    }

    @Override
    public boolean isCommunity() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Bringing process automation to Micronaut: Embed the Camunda Platform 7 Workflow Engine";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.exclude(Netty.class::isInstance);
        featureContext.addFeatureIfNotPresent(DatabaseDriverFeature.class, defaultDbFeature);
        featureContext.addFeatureIfNotPresent(Jetty.class, jetty);
        featureContext.addFeatureIfNotPresent(AssertJ.class, assertJ);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addConfiguration(generatorContext);
        generatorContext.addDependency(DEPENDENCY_PLATFORM7);
        generatorContext.addDependency(DEPENDENCY_BPM_ASSERT);
    }

    @Override
    public String getCategory() {
        return Category.BPM;
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://github.com/camunda-community-hub/micronaut-camunda-platform-7";
    }

    protected static void addConfiguration(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("camunda.admin-user.id", "admin");
        generatorContext.getConfiguration().put("camunda.admin-user.password", "admin");
        generatorContext.getConfiguration().put("camunda.webapps.enabled", true);
        generatorContext.getConfiguration().put("camunda.rest.enabled", true);
        generatorContext.getConfiguration().put("camunda.generic-properties.properties.initialize-telemetry", true);
        generatorContext.getConfiguration().put("camunda.filter.create", "All tasks");
    }

}
