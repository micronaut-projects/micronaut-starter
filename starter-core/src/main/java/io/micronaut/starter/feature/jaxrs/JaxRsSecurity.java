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
package io.micronaut.starter.feature.jaxrs;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.jax.rs.security.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class JaxRsSecurity implements Feature, MicronautServerDependent {
    private static final String ARTIFACT_ID_MICRONAUT_JAXRS_SERVER_SECURITY = "micronaut-jaxrs-server-security";

    @Override
    public String getName() {
        return "jax-rs-security";
    }

    @Override
    public String getTitle() {
        return "JAX-RS Micronaut Security support";
    }

    @Override
    public String getDescription() {
        return "Integrates Micronaut Security with JAX-RS";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.jaxrsDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_JAXRS_SERVER_SECURITY)
                .versionProperty("micronaut.jaxrs.version")
                .compile());
    }

    @Override
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.SERVER;
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html";
    }
}
