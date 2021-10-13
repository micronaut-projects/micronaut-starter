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
package io.micronaut.starter.feature.logging;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.logging.template.log4j2;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

@Singleton
public class Log4j2 implements LoggingFeature {

    @Override
    public String getName() {
        return "log4j2";
    }

    @Override
    public String getTitle() {
        return "Log4j 2 Logging";
    }

    @Override
    public String getDescription() {
        return "Adds Log4j 2 Logging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("log4j-core").compile());
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("log4j-api").runtime());
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("log4j-slf4j-impl").runtime());
        generatorContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/log4j2.xml", log4j2.template(generatorContext.getProject())));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
