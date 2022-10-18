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
public class Hamcrest  implements JunitCompanionFeature {
    public static final String GROUP_ID_HAMCREST = "org.hamcrest";
    public static final String FEATURE_HAMCREST = "hamcrest";
    public static final String ARTIFACT_ID_HAMCREST = "hamcrest";
    public static final Dependency DEPENDENCY_HAMCREST = Dependency.builder()
            .groupId(GROUP_ID_HAMCREST)
            .artifactId(ARTIFACT_ID_HAMCREST)
            .test()
            .build();

    @Override
    @NonNull
    public String getName() {
        return FEATURE_HAMCREST;
    }

    @Override
    public String getTitle() {
        return "Hamcrest";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Hamcrest matchers for JUnit";
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
        return "https://hamcrest.org/JavaHamcrest/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Hamcrest.DEPENDENCY_HAMCREST);
    }
}
