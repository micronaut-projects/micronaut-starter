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
package io.micronaut.starter.feature.aop;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class AOP implements Feature {
    public static final String NAME = "micronaut-aop";

    private static final Dependency MICRONAUT_INJECT_JAVA_DEPENDENCY = MicronautDependencyUtils
            .coreDependency()
            .artifactId("micronaut-inject-java")
            .annotationProcessor()
            .versionProperty("micronaut.version")
            .order(Priority.MICRONAUT_INJECT_JAVA.getOrder())
            .build();

    private static final Dependency MICRONAUT_AOP_DEPENDENCY = MicronautDependencyUtils
            .coreDependency()
            .artifactId("micronaut-aop")
            .compile()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Aspect-Oriented Programming (AOP)";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Aspect-Oriented Programming (AOP) API";
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_INJECT_JAVA_DEPENDENCY);
        generatorContext.addDependency(MICRONAUT_AOP_DEPENDENCY);
    }

        @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#aop";
    }
}
