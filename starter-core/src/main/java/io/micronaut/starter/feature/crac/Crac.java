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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.RequireEagerSingletonInitializationFeature;
import io.micronaut.starter.feature.database.jdbc.Hikari;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.crac.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
    @NonNull
    public String getDescription() {
        return "Allows building an application that supports CRaC (Coordinated Restore at Checkpoint)";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://wiki.openjdk.org/display/CRaC";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-crac/latest/guide";
    }

    @Override
    public boolean supports(Options options) {
        return options instanceof MicronautOptions micronautOptions &&
                (micronautOptions.applicationType() == ApplicationType.DEFAULT || micronautOptions.applicationType() == ApplicationType.CLI);
    }

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_CRAC);
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
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
