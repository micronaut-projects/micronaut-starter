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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.URLTemplate;

import jakarta.inject.Singleton;

@Singleton
public class KoTest implements TestFeature {
    protected static final String ARTIFACT_ID_MICRONAUT_TEST_KOTEST = "micronaut-test-kotest";

    protected static final Dependency DEPENDENCY_MICRONAUT_TEST_KOTEST = MicronautDependencyUtils
            .testDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_TEST_KOTEST)
            .test()
            .build();
    protected static final String ARTIFACT_ID_KOTEST_RUNNER_JUNIT_5_JVM = "kotest-runner-junit5-jvm";

    protected static final String ARTIFACT_ID_KOTEST_ASSERTIONS_CORE_JVM = "kotest-assertions-core-jvm";

    protected final Mockk mockk;

    public KoTest(Mockk mockk) {
        this.mockk = mockk;
    }

    @Override
    @NonNull
    public String getName() {
        return "kotest";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (
                !featureContext.isPresent(Mockk.class)
                && featureContext.getBuildTool() == BuildTool.MAVEN
        ) {
            featureContext.addFeature(mockk);
        }
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("koTestConfig",
                new URLTemplate("src/test/kotlin/io/kotest/provided/ProjectConfig.kt",
                        classLoader.getResource("kotest/ProjectConfig.kt")));

        // Only for Maven, these dependencies are applied by the Micronaut Gradle Plugin
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_TEST_KOTEST);
            generatorContext.addDependency(Dependency.builder()
                    .lookupArtifactId(ARTIFACT_ID_KOTEST_RUNNER_JUNIT_5_JVM)
                    .test());
            generatorContext.addDependency(Dependency.builder()
                    .lookupArtifactId(ARTIFACT_ID_KOTEST_ASSERTIONS_CORE_JVM)
                    .test());
        }
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.KOTEST;
    }

}
