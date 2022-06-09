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
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicronautRestAssured implements Feature {
    private static final String ARTIFACT_ID_MICRONAUT_TEST_REST_ASSURED = "micronaut-test-rest-assured";

    @NonNull
    @Override
    public String getName() {
        return "micronaut-test-rest-assured";
    }

    @Override
    public String getTitle() {
        return "Micronaut-Test REST-assured";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "A small Micronaut-Test utility module that helps integrate the REST-assured library";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.testDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_TEST_REST_ASSURED)
                .test());
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-test/latest/guide/#restAssured";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://rest-assured.io/#docs";
    }
}
