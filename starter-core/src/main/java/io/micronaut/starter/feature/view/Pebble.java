/*
 * Copyright 2017-2021 original authors
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
public class Pebble implements ViewFeature, MicronautServerDependent {

    private static final String ARTIFACT_ID_MICRONAUT_VIEWS_PEBBLE = "micronaut-views-pebble";

    @Override
    public String getName() {
        return "views-pebble";
    }

    @Override
    public String getTitle() {
        return "Pebble Views";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using Pebble";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://pebbletemplates.io/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#pebble";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.viewsDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_PEBBLE)
                .compile());
    }
}
