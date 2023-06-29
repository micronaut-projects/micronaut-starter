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

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

import static io.micronaut.starter.springboot.SpringBootDependencies.DEPENDENCY_SPRINGBOOT_STARTER_TEST;
import static io.micronaut.starter.springboot.SpringBootDependencies.DEPENDENCY_SPRING_BOOT_STARTER_WEB;

@Singleton
public class SpringBootStarterWeb implements SpringBootStarterFeature {
    public static final String NAME = "spring-boot-starter-web";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Web";
    }

    @Override
    public String getDescription() {
        return "Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_SPRING_BOOT_STARTER_WEB);
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_STARTER_TEST);
    }

}
