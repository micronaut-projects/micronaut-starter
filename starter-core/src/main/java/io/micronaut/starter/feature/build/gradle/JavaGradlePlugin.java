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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.java.gradle.plugin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.gradle.org/current/userguide/java_plugin.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("java")
                    .build());
        }
    }
}
