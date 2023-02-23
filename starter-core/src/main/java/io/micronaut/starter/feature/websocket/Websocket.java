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
package io.micronaut.starter.feature.websocket;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class Websocket implements Feature {

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getName() {
        return "websocket";
    }

    @Override
    public String getTitle() {
        return "Websocket";
    }

    @Override
    public String getDescription() {
        return "Adds support for creating WebSocket clients and servers.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-websocket")
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/#websocket";
    }

    @Override
    public String getCategory() {
        return Category.SERVER;
    }
}
