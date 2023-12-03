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
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

@Singleton
public class SpringWeb extends SpringFeature implements MicronautServerDependent {

    public static final String NAME = "spring-web";

    public SpringWeb(Spring spring) {
        super(spring);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring MVC Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring MVC Controller Annotations";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder springWebAnnotation = MicronautDependencyUtils.springDependency()
                .artifactId("micronaut-spring-web-annotation")
                .versionProperty("micronaut.spring.version")
                .template();

        generatorContext.addDependency(springWebAnnotation.annotationProcessor());
        generatorContext.addDependency(springWebAnnotation.testAnnotationProcessor());
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.springframework.boot")
                .artifactId("spring-boot-starter-web")
                .compile());
        generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-http-server")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.spring")
                .artifactId("micronaut-spring-web")
                .runtime());
    }
}
