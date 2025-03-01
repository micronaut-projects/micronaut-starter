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
package io.micronaut.starter.feature.view;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.views.velocity.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Velocity implements ViewFeature, MicronautServerDependent {

    public static final String ARTIFACT_ID_MICRONAUT_VIEWS_VELOCITY = "micronaut-views-velocity";

    @Override
    public String getName() {
        return "views-velocity";
    }

    @Override
    public String getTitle() {
        return "Velocity Views";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using Apache Velocity";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://velocity.apache.org";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#velocity";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.viewsDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_VELOCITY)
                .compile());
    }
}
