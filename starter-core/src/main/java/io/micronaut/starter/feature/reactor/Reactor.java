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
package io.micronaut.starter.feature.reactor;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.other.HttpClient;
import io.micronaut.starter.feature.reactive.ReactiveFeature;
import jakarta.inject.Singleton;

@Singleton
public class Reactor implements ReactiveFeature {

    public static final Dependency MICRONAUT_REACTOR_DEPENDENCY = MicronautDependencyUtils.reactorDependency()
            .artifactId("micronaut-reactor")
            .compile()
            .build();

    public static final Dependency MICROMETER_CONTEXT_PROPOGRATION_DEPENDENCY = Dependency.builder()
            .groupId("io.micrometer")
            .artifactId("context-propagation")
            .compile()
            .build();

    private final ReactorHttpClient reactorHttpClient;

    public Reactor(ReactorHttpClient reactorHttpClient) {
        this.reactorHttpClient = reactorHttpClient;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "reactor";
    }

    @Override
    public String getTitle() {
        return "Reactor";
    }

    @Override
    public String getDescription() {
        return "Adds Reactive support using Project Reactor";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-reactor/snapshot/guide/index.html";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(HttpClient.class)) {
            featureContext.addFeature(reactorHttpClient);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_REACTOR_DEPENDENCY);
        generatorContext.addDependency(MICROMETER_CONTEXT_PROPOGRATION_DEPENDENCY);
    }
}
