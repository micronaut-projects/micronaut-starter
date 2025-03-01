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
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.BuildTool;

import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.jetty.server.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Jetty extends AbstractServletFeature {

    @Override
    public String getName() {
        return "jetty-server";
    }

    @Override
    public String getTitle() {
        return "Jetty Server";
    }

    @Override
    public String getDescription() {
        return "Adds support for a Jetty server";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-servlet/latest/guide/index.html#jetty";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.micronaut.servlet")
                    .artifactId("micronaut-http-server-jetty")
                    .compile());
        }
    }

    @Override
    @NonNull
    public String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        return "jetty";
    }
}
