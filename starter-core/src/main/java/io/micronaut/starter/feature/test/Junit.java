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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.TestFramework;

import io.micronaut.starter.template.PropertiesTemplate;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Requires(property = "micronaut.starter.feature.junit.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Junit implements TestFeature {
    private static final String GROUP_ID_JUNIT_PLATFORM = "org.junit.platform";
    private static final String ARTIFACT_ID_JUNIT_PLATFORM_LAUNCHER = "junit-platform-launcher";
    private static final Dependency DEPENDENCY_JUNIT_PLATFORM_LAUNCHER = Dependency.builder()
            .groupId(GROUP_ID_JUNIT_PLATFORM)
            .artifactId(ARTIFACT_ID_JUNIT_PLATFORM_LAUNCHER)
            .testRuntime()
            .build();

    private static final String GROUP_ID_JUNIT_JUPITER = "org.junit.jupiter";
    private static final String ARTIFACT_ID_JUNIT_JUPITER_API = "junit-jupiter-api";
    private static final String ARTIFACT_ID_JUNIT_JUPITER_ENGINE = "junit-jupiter-engine";

    private static final String ARTIFACT_ID_MICRONAUT_TEST_JUNIT_5 = "micronaut-test-junit5";

    private static final Dependency DEPENDENCY_JUNIT_JUPITER_API = Dependency.builder()
            .groupId(GROUP_ID_JUNIT_JUPITER)
            .artifactId(ARTIFACT_ID_JUNIT_JUPITER_API)
            .test()
            .build();

    private static final Dependency DEPENDENCY_JUNIT_JUPITER_ENGINE = Dependency.builder()
            .groupId(GROUP_ID_JUNIT_JUPITER)
            .artifactId(ARTIFACT_ID_JUNIT_JUPITER_ENGINE)
            .test()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_TEST_JUNIT5 = MicronautDependencyUtils
            .testDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_TEST_JUNIT_5)
            .test()
            .build();
    private final List<JunitPlatformPropertyProvider> junitPlatformPropertyProviders;

    public Junit(List<JunitPlatformPropertyProvider> junitPlatformPropertyProviders) {
        this.junitPlatformPropertyProviders = junitPlatformPropertyProviders;
    }

    @Override
    @NonNull
    public String getName() {
        return "junit";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        // Only for Maven, these dependencies are applied by the Micronaut Gradle Plugin
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(DEPENDENCY_JUNIT_JUPITER_API);
            generatorContext.addDependency(DEPENDENCY_JUNIT_JUPITER_ENGINE);
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_TEST_JUNIT5);
        } else {
            generatorContext.addDependency(DEPENDENCY_JUNIT_PLATFORM_LAUNCHER);
        }
        junitPlatformProperties(generatorContext);
    }

    private void junitPlatformProperties(GeneratorContext generatorContext) {
        Map<String, Object> junitPlatformProperties = new HashMap<>();
        for (JunitPlatformPropertyProvider provider : junitPlatformPropertyProviders) {
            junitPlatformProperties.putAll(provider.getJunitPlatformProperties());
        }
        if (CollectionUtils.isNotEmpty(junitPlatformProperties)) {
            generatorContext.addTemplate("junit-platform.properties",
                    new PropertiesTemplate("src/test/resources/junit-platform.properties", junitPlatformProperties));
        }
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.JUNIT;
    }
}
