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
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Set;

@Requires(property = "micronaut.starter.feature.micronaut-test-netty-leak.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class MicronautTestNettyLeak implements DefaultFeature, JunitPlatformPropertyProvider {
    private static final Dependency DEPENDENCY_MICRONAUT_TEST_NETTY_LEAK = MicronautDependencyUtils.testDependency()
            .artifactId("micronaut-test-netty-leak")
            .testRuntime()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "test-netty-leak";
    }

    @Override
    public String getTitle() {
        return "Micronaut Test Netty Leak";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Light-weight Netty leak detection mechanism specifically designed for tests";
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
        return "https://netty.io/4.2/api/io/netty/util/LeakPresenceDetector.html";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-test/latest/guide/#nettyLeak";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext);
    }

    protected void addDependency(GeneratorContext generatorContext) {
        if (generatorContext.hasFeature(Netty.class)) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_TEST_NETTY_LEAK);
        }
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getTestFramework() == TestFramework.JUNIT || options.getTestFramework() == TestFramework.SPOCK;
    }

    @Override
    public Map<String, Object> getJunitPlatformProperties() {
        return Map.of("junit.jupiter.extensions.autodetection.enabled", true);
    }
}
