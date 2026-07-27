/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.cdi;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.langchain4j.Langchain4jFeature;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.opendi.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OpenDi implements Langchain4jFeature {

    public static final String NAME = "opendi";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Open DI";
    }

    @Override
    public String getDescription() {
        return "CDI Lite implementation backed by Micronaut";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/eclipse-ee4j/odi";
    }

    @Override
    public String getCategory() {
        return Category.AI;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("micronaut-odi-processor-cdi").annotationProcessor());
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("micronaut-odi-cdi").compile());
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("jakarta.enterprise.cdi-api").compile());
    }
}
