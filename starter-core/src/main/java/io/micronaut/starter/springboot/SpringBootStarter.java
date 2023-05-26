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
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class SpringBootStarter implements SpringDefaultFeature {
    public static final String NAME = "spring-boot-starter";
    private static final String GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
    private static final String ARTIFACT_ID_SPRING_BOOT_STARTER = "spring-boot-starter";
    private static final Dependency.Builder DEPENDENCY_SPRINGBOOT_STARTER = Dependency.builder()
            .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER)
            .compile();
    private static final String ARTIFACT_ID_SPRING_BOOT_STARTER_TEST = "spring-boot-starter-test";
    private static final Dependency.Builder DEPENDENCY_SPRINGBOOT_STARTER_TEST = Dependency.builder()
            .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER_TEST)
            .test();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Boot Starter";
    }

    @Override
    public String getDescription() {
        return "Adds Spring Boot Starter dependencies";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_STARTER);
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_STARTER_TEST);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }
}
