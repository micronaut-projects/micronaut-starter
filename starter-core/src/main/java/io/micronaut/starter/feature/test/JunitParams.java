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
package io.micronaut.starter.feature.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class JunitParams implements JunitCompanionFeature {

    @Override
    @NonNull
    public String getName() {
        return "junit-params";
    }

    @Override
    public String getTitle() {
        return "JUnit Params";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Provides easy parameterization of test methods in JUnit tests.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext);
    }

    protected void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-params")
                .test()
        );
    }
}
