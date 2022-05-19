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
package io.micronaut.starter.feature.tracing;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.server.MicronautServerDependent;

import jakarta.inject.Singleton;

@Singleton
public class Zipkin implements TracingFeature, MicronautServerDependent {

    @NonNull
    @Override
    public String getName() {
        return "tracing-zipkin";
    }

    @Override
    public String getTitle() {
        return "Zipkin Tracing";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for distributed tracing with Zipkin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.tracing")
                .artifactId("micronaut-tracing-zipkin")
                .compile());

        generatorContext.getConfiguration().put("tracing.zipkin.enabled", true);
        generatorContext.getConfiguration().put("tracing.zipkin.http.url", "http://localhost:9411");
        generatorContext.getConfiguration().put("tracing.zipkin.sampler.probability", 0.1);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://zipkin.io/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#zipkin";
    }
}
