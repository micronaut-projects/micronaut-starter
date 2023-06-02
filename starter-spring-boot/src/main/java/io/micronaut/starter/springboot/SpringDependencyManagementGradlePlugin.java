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
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.GradleSpecificFeature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class SpringDependencyManagementGradlePlugin implements GradleSpecificFeature, SpringDefaultFeature {
    private static final String ARTIFACT_ID = "dependency-management-plugin";
    private static final String GRADLE_PLUGIN_ID_SPRING_DEPENDENCY_MANAGEMENT = "io.spring.dependency-management";

    @Override
    public String getName() {
        return "spring-dependency-management-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Spring Dependency Management Plugin Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Spring Dependency Management Plugin Gradle Plugin that provides Maven-like dependency management and exclusions.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/dependency-management-plugin/docs/current-SNAPSHOT/reference/html/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id(GRADLE_PLUGIN_ID_SPRING_DEPENDENCY_MANAGEMENT)
                    .lookupArtifactId(ARTIFACT_ID)
                    .build());
        }
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }

}
