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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.logging.template.log4j2;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

@Singleton
public class Log4j2 implements LoggingFeature {
    public static final String NAME = "log4j2";

    private static final String GROUP_ID = "org.apache.logging.log4j";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Log4j 2 Logging";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Log4j 2 Logging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        populateDependencies(generatorContext);
        generatorContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/log4j2.xml", log4j2.template(generatorContext.getProject())));
    }

    private void populateDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID)
                .artifactId("log4j-bom")
                .version(VersionInfo.getBomVersion("log4j"))
                .pom()
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID)
                .artifactId("log4j-api")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID)
                .artifactId("log4j-core")
                .runtime());
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID)
                .artifactId("log4j-slf4j-impl")
                .runtime());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
