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
package io.micronaut.starter.feature.spring;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class Spring implements Feature {
    public static final String NAME = "spring";
    private static final String ARTIFACT_ID_MICRONAUT_SPRING_ANNOTATION = "micronaut-spring-annotation";
    private static final String PROPERTY_MICRONAUT_SPRING_VERSION = "micronaut.spring.version";
    private static final String GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
    private static final String ARTIFACT_ID_SPRING_BOOT_STARTER = "spring-boot-starter";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Framework Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring Framework Annotations";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(
                MicronautDependencyUtils.annotationProcessor(generatorContext.getBuildTool(), MicronautDependencyUtils.GROUP_ID_MICRONAUT_SPRING, ARTIFACT_ID_MICRONAUT_SPRING_ANNOTATION, PROPERTY_MICRONAUT_SPRING_VERSION));
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
                .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER)
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.SPRING;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-spring/latest/guide/index.html";
    }
}
