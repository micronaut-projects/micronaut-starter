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

import javax.inject.Singleton;

@Singleton
public class RxJavaThree implements Feature {
    public static final String MICRONAUT_RXJAVA3_GROUP_ID = "io.micronaut.rxjava3";

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "rxjava3";
    }

    @Override
    public String getTitle() {
        return "RxJava 3";
    }

    @Override
    public String getDescription() {
        return "Adds support for RxJava 3 to a Micronaut application; Converters and Instrumentation for RxJava 3 types, RxJava 3 compatible HTTP Client";
    }

    @Override
    public String getCategory() {
        return Category.REACTIVE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-rxjava3/snapshot/guide/index.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(MICRONAUT_RXJAVA3_GROUP_ID)
                .artifactId("micronaut-rxjava3")
                .compile());
    }
}
