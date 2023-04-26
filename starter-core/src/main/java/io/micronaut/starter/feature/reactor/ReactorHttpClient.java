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
package io.micronaut.starter.feature.reactor;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.reactive.ReactiveHttpClientFeature;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class ReactorHttpClient implements ReactiveHttpClientFeature {
    public static final String ARTIFACT_ID_MICRONAUT_REACTOR_HTTP_CLIENT = "micronaut-reactor-http-client";
    public static final Dependency DEPENDENCY_MICRONAUT_REACTOR_HTTP_CLIENT = Dependency.builder()
            .groupId(Reactor.MICRONAUT_REACTOR_GROUP_ID)
            .artifactId(ARTIFACT_ID_MICRONAUT_REACTOR_HTTP_CLIENT)
            .compile()
            .build();

    @NonNull
    @Override
    public String getName() {
        return "reactor-http-client";
    }

    @Override
    public String getTitle() {
        return "Reactor HTTP Client";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Version of the Http client which supports Project Reactor";
    }

    @Override
    public List<Dependency> getDependencies(GeneratorContext generatorContext) {
        return Collections.singletonList(DEPENDENCY_MICRONAUT_REACTOR_HTTP_CLIENT);
    }
}
