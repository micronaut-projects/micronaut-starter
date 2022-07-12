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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;

import static io.micronaut.starter.options.BuildTool.MAVEN;

@Singleton
public class KoTest implements TestFeature {
    private static final String ARTIFACT_ID_MICRONAUT_KOTEST5 = "micronaut-test-kotest5";
    private static final String KOTLINX_VERSION = "1.6.2";

    @Override
    public String getName() {
        return "kotest";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("koTestConfig",
                new URLTemplate("src/test/kotlin/io/kotest/provided/ProjectConfig.kt",
                        classLoader.getResource("kotest/ProjectConfig.kt")));

        if (generatorContext.getBuildTool() == MAVEN) {
            // TODO Maven tests fail without this due to older, incompatible version
            //  but I think it can be removed after everything else updates to Kotest5 and removes Kotest4
            generatorContext.addDependency(new Dependency.Builder()
                    .groupId("org.jetbrains.kotlinx")
                    .artifactId("kotlinx-coroutines-core-jvm")
                    .version(KOTLINX_VERSION)
                    .test());

            // for Gradle, these are exposed by the micronaut-gradle-plugin
            generatorContext.addDependency(new Dependency.Builder()
                    .groupId("io.mockk")
                    .artifactId("mockk")
                    .test());
            generatorContext.addDependency(MicronautDependencyUtils.testDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_KOTEST5)
                    .test());
            generatorContext.addDependency(new Dependency.Builder()
                    .groupId("io.kotest")
                    .artifactId("kotest-assertions-core-jvm")
                    .test());
            generatorContext.addDependency(new Dependency.Builder()
                    .groupId("io.kotest")
                    .artifactId("kotest-runner-junit5-jvm")
                    .testRuntime());
        }
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.KOTEST;
    }

    @Override
    public String getTitle() {
        return "Micronaut Test Kotest5";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-test/latest/guide/#kotest5";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://kotest.io/";
    }
}
