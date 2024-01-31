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
package io.micronaut.starter.feature.httpclient;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class HttpClientJdk implements HttpClientFeature {
    public static final String NAME = "http-client-jdk";

    public static final String ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK = "micronaut-http-client-jdk";
    private static final Dependency.Builder MICRONAUT_HTTP_CLIENT_JDK = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK);
    public static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK = MICRONAUT_HTTP_CLIENT_JDK
            .compile()
            .build();
    public static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK_COMPILE_ONLY = MICRONAUT_HTTP_CLIENT_JDK
            .compileOnly()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "HTTP Client Jdk";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Micronaut HTTP client based on the Java HTTP Client";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#jdkHttpClient";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://openjdk.org/groups/net/httpclient/intro.html";
    }

    @Override
    @NonNull
    public List<Dependency> getDependencies(@NonNull GeneratorContext generatorContext) {
        return Collections.singletonList(DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK);
    }
}
