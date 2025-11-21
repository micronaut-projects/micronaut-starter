/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import static io.micronaut.core.util.StringUtils.TRUE;
import static io.micronaut.starter.feature.Category.VALIDATION;

@Requires(property = "micronaut.starter.feature.jspecify.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class Jspecify implements Feature {

    public static final String NAME = "jspecify";
    private static final Dependency JSPECIFY_DEPENDENCY =
            Dependency.builder()
                    .groupId("org.jspecify")
                    .artifactId("jspecify")
                    .compile()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JSpecify Nullability Annotations";
    }

    @Override
    public String getDescription() {
        return "Micronaut supports JSpecify Nullability Annotations.";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/#jspecify";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://jspecify.dev/docs/start-here/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return VALIDATION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(JSPECIFY_DEPENDENCY);
    }
}
