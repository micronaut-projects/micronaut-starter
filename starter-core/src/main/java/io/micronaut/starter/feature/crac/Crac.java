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
package io.micronaut.starter.feature.crac;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.RequireEagerSingletonInitializationFeature;
import io.micronaut.starter.feature.database.jdbc.Hikari;
import jakarta.inject.Singleton;

@Singleton
public class Crac implements RequireEagerSingletonInitializationFeature {

    public static final String NAME = "crac";

    public static final Dependency DEPENDENCY_MICRONAUT_CRAC = MicronautDependencyUtils.cracDependency()
            .artifactId("micronaut-crac")
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Support for CRaC (Coordinated Restore at Checkpoint)";
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Allows building an application that supports CRaC (Coordinated Restore at Checkpoint)";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://wiki.openjdk.org/display/CRaC";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-crac/latest/guide";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.CLI;
    }

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_CRAC);
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("io.micronaut.crac")
                    .lookupArtifactId("micronaut-crac-plugin")
                    .build());
        }
        if (generatorContext.isFeaturePresent(Hikari.class)) {
            generatorContext.getConfiguration().addNested("datasources.default.allow-pool-suspension", true);
        }
    }
}
