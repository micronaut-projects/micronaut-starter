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
package io.micronaut.starter.feature.jib;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;

@Singleton
public class Jib implements Feature {

    @Override
    public String getName() {
        return "jib";
    }

    @Override
    public String getTitle() {
        return "Jib Docker Containers";
    }

    @Override
    public String getDescription() {
        return "Build Docker containers using Jib";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addHelpLink("Jib Gradle Plugin", "https://plugins.gradle.org/plugin/com.google.cloud.tools.jib");
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.google.cloud.tools.jib")
                    .lookupArtifactId("jib-gradle-plugin")
                    .build());
        }
    }
}
