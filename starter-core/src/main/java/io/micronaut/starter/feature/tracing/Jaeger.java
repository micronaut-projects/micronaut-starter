/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.tracing;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.server.MicronautServerDependent;

import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

@Singleton
public class Jaeger implements TracingFeature, MicronautServerDependent {

    public static final String NAME = "tracing-jaeger";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Jaeger Tracing";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for distributed tracing with Jaeger";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("tracing.jaeger.enabled", true);
        generatorContext.getConfiguration().put("tracing.jaeger.sampler.probability", 0.1);

        generatorContext.addDependency(MicronautDependencyUtils.tracingDependency()
                .artifactId("micronaut-tracing-jaeger")
                .compile());

        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.GROOVY) {
            generatorContext.addDependency(MicronautDependencyUtils.coreProcessor().compileOnly());
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#jaeger";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.jaegertracing.io/";
    }
}
