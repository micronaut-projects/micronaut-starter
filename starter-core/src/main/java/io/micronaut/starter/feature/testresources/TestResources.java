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
package io.micronaut.starter.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.gradle.MicronautTestResourcesGradlePlugin;
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
public class TestResources implements Feature {

    public static final String MICRONAUT_TEST_RESOURCES_ENABLED = "micronaut.test.resources.enabled";

    @Override
    @NonNull
    public String getName() {
        return "test-resources";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Micronaut Test Resources";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for managing external resources which are required during development or testing.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(MicronautTestResourcesGradlePlugin.builder().build());
        } else {
            BuildProperties buildProperties = generatorContext.getBuildProperties();
            buildProperties.put(MICRONAUT_TEST_RESOURCES_ENABLED, StringUtils.TRUE);

            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.micronaut.testresources")
                    .artifactId("micronaut-test-resources-client")
                    .test());
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/";
    }
}
