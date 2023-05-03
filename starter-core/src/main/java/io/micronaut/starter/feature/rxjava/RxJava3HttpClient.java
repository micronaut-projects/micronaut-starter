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
package io.micronaut.starter.feature.rxjava;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.reactive.ReactiveHttpClientFeature;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class RxJava3HttpClient implements ReactiveHttpClientFeature {

    private static final String ARTIFACT_ID_MICRONAUT_RXJAVA_3_HTTP_CLIENT = "micronaut-rxjava3-http-client";
    private static final Dependency DEPENDENCY_MICRONAUT_RXJAVA3_HTTP_CLIENT = Dependency.builder()
            .groupId(RxJava3.MICRONAUT_RXJAVA3_GROUP_ID)
            .artifactId(ARTIFACT_ID_MICRONAUT_RXJAVA_3_HTTP_CLIENT)
            .compile().build();

    @Override
    public List<Dependency> getDependencies(GeneratorContext generatorContext) {
        return Collections.singletonList(DEPENDENCY_MICRONAUT_RXJAVA3_HTTP_CLIENT);
    }

    @NonNull
    @Override
    public String getName() {
        return "rxjava3-http-client";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "RxJava 3 HTTP Client";
    }

    @Override
    public String getDescription() {
        return "RxJava 3 variation of the Micronaut HTTP client.";
    }
}
