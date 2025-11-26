/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.starter.feature;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

@Singleton
public class MicronautProcessingCompilerArgs implements CompilerArgCodeContributingFeature, DefaultFeature {
    @Override
    public List<String> getCompilerArgs(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        return List.of(
                "-Amicronaut.processing.group=" + project.getPackageName(),
                "-Amicronaut.processing.module=" + project.getName()
                );
    }

    @Override
    public String getName() {
        return "micronaut-processing-compiler-args";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool() == BuildTool.MAVEN;
    }
}
