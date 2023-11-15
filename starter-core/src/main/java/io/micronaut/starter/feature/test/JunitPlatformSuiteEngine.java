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
package io.micronaut.starter.feature.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class JunitPlatformSuiteEngine implements JunitCompanionFeature {

    private static final String ARTIFACT_ID_JUNIT_PLATFORM_SUITE_ENGINE = "junit-platform-suite-engine";
    private static final String GROUP_ID_ORG_JUNIT_PLATFORM = "org.junit.platform";
    public static final Dependency.Builder DEPENDENCY_JUNIT_PLATFORM_SUITE_ENGINE = Dependency.builder()
            .groupId(GROUP_ID_ORG_JUNIT_PLATFORM)
            .artifactId(ARTIFACT_ID_JUNIT_PLATFORM_SUITE_ENGINE)
            .test();

    @Override
    @NonNull
    public String getName() {
        return "junit-platform-suite-engine";
    }

    @Override
    public String getTitle() {
        return "JUnit Platform Suite Engine";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the junit-platform-suite-engine dependency, an implementation of the TestEngine API for declarative test suites.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.TEST;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://junit.org/junit5/docs/current/user-guide/#junit-platform-suite-engine-setup";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_JUNIT_PLATFORM_SUITE_ENGINE);
    }
}
