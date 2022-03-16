/*
 * Copyright 2017-2020 original authors
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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class Spring implements Feature {

    @Override
    public String getName() {
        return "spring";
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
        Dependency.Builder springAnnotation = Dependency.builder()
                .groupId("io.micronaut.spring")
                .artifactId("micronaut-spring-annotation")
                .versionProperty("micronaut.spring.version")
                .template();

        generatorContext.addDependency(springAnnotation.annotationProcessor());
        generatorContext.addDependency(springAnnotation.testAnnotationProcessor());
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.springframework.boot")
                .artifactId("spring-boot-starter")
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
