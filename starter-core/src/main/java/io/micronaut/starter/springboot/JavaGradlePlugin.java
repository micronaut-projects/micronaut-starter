/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.springboot;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.GradleSpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class JavaGradlePlugin implements GradleSpecificFeature {
    @Override
    public String getName() {
        return "java-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Java Gradle Plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Adds the Java Gradle Plugin which adds Java compilation along with testing and bundling capabilities to a project.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.gradle.org/current/userguide/java_plugin.html";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("java")
                    .build());
        }
    }
}
