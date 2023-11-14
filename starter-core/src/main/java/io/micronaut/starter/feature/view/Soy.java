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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.server.MicronautServerDependent;

import jakarta.inject.Singleton;

@Singleton
public class Soy implements ViewFeature, MicronautServerDependent {

    private static final String ARTIFACT_ID_MICRONAUT_VIEWS_SOY = "micronaut-views-soy";

    @Override
    public String getName() {
        return "views-soy";
    }

    @Override
    public String getTitle() {
        return "Soy Yiews";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using Soy (Closure Templates)";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/google/closure-templates";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#soy";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.viewsDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_SOY)
                .compile());
    }
}
