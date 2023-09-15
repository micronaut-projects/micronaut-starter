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
package io.micronaut.starter.feature.spring;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

@Singleton
public class SpringBoot extends SpringFeature {

    public static final String NAME = "spring-boot";

    public SpringBoot(Spring spring) {
        super(spring);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Boot Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring Boot Annotations";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder springBoot = Dependency.builder()
                .groupId("io.micronaut.spring")
                .artifactId("micronaut-spring-boot-annotation")
                .versionProperty("micronaut.spring.version")
                .template();

        generatorContext.addDependency(springBoot.annotationProcessor());
        generatorContext.addDependency(springBoot.testAnnotationProcessor());
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.springframework.boot")
                .artifactId("spring-boot-starter-web")
                .compile());
        Dependency.Builder micronautSpringBoot = Dependency.builder()
                .groupId("io.micronaut.spring")
                .artifactId("micronaut-spring-boot")
                .runtime();
        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.GROOVY) {
            micronautSpringBoot = micronautSpringBoot.compile();
        }
        generatorContext.addDependency(micronautSpringBoot);
    }
}
