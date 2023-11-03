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
package io.micronaut.starter.feature.logging;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.logging.template.julToSlf4j;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
public class Slf4jJulBridge implements LoggingFeature {

    private static final boolean USE_JUL = true;
    private final Logback logback;

    public Slf4jJulBridge(Logback logback) {
        this.logback = logback;
    }

    @Override
    @NonNull
    public String getName() {
        return "jul-to-slf4j";
    }

    @Override
    public String getTitle() {
        return "SLF4J JUL Bridge";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Java Util Logging bridge for SLF4J with Logback.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.slf4j.org/legacy.html#jul-to-slf4jBridge";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        logback.addConfig(generatorContext, USE_JUL);
        addLoggingProperties(generatorContext);
        logback.addDependency(generatorContext);
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("jul-to-slf4j").runtime());
    }

    protected void addLoggingProperties(GeneratorContext generatorContext) {
        generatorContext.addTemplate("loggingProperties", new RockerTemplate("src/main/resources/logging.properties", julToSlf4j.template()));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
