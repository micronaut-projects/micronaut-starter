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
package io.micronaut.starter.feature.other;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.httpclient.HttpClientFeature;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class HttpClient implements HttpClientFeature {
    public static final String NAME = "http-client";
    public static final String ARTIFACT_ID_MICRONAUT_HTTP_CLIENT = "micronaut-http-client";
    public static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT)
            .compile()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "HTTP Client";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Micronaut HTTP client";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient";
    }

    @Override
    @NonNull
    public List<Dependency> getDependencies(@NonNull GeneratorContext generatorContext) {
        return Collections.singletonList(DEPENDENCY_MICRONAUT_HTTP_CLIENT);
    }
}
