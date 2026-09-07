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
package io.micronaut.starter.feature.server;

import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.JvmFeature;
import org.jspecify.annotations.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.http.poja.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class HttpPoja extends AbstractMicronautServerFeature implements JvmFeature {

    private static final String ARTIFACT_ID_MICRONAUT_HTTP_POJA_APACHE = "micronaut-http-poja-apache";
    private static final String ARTIFACT_ID_MICRONAUT_HTTP_POJA_TEST = "micronaut-http-poja-test";

    @Override
    public String getName() {
        return "http-poja";
    }

    @Override
    public String getTitle() {
        return "Plain Old Java HTTP Application";
    }

    @Override
    public String getDescription() {
        return "Add support for HTTP POJA based on Apache libraries";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-servlet/latest/guide/index.html#httpPoja";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(MicronautDependencyUtils.servletDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_POJA_APACHE)
                    .compile());
            generatorContext.addDependency(MicronautDependencyUtils.servletDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_POJA_TEST)
                    .test());
        }
    }

    @Override
    @NonNull
    public String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        return "http_poja";
    }
}
