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
package io.micronaut.starter.feature.logging;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.logging.template.slf4jSimple;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

@Singleton
public class SimpleLogging implements LoggingFeature {
    @Override
    public String getName() {
        return "slf4j-simple";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/simplelogger.properties", slf4jSimple.template()));
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.slf4j")
                .artifactId("slf4j-simple")
                .runtime());
    }

    @Override
    public String getTitle() {
        return "SLF4J Simple Logging";
    }

    @Override
    public String getDescription() {
        return "Adds SLF4J Simple Logging through simplelogger.properties";
    }

    @Override
    public String getCategory() {
        return Category.LOGGING;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
