/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.rxjava;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;

@Singleton
public class RxJavaTwo implements Feature {
    public static final String MICRONAUT_RXJAVA2_GROUP_ID = "io.micronaut.rxjava2";

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "rxjava2";
    }

    @Override
    public String getTitle() {
        return "RxJava 2";
    }

    @Override
    public String getDescription() {
        return "Adds support for RxJava 2 to a Micronaut application; Converters and Instrumentation for RxJava 2 types, RxJava 2 compatible HTTP Client";
    }

    @Override
    public String getCategory() {
        return Category.REACTIVE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-rxjava2/snapshot/guide/index.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(MICRONAUT_RXJAVA2_GROUP_ID)
                .artifactId("micronaut-rxjava2")
                .compile());
    }

}
