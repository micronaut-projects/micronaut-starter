/*
 * Copyright 2017-2026 original authors
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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.options.TestFramework;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.pytest.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Pytest implements TestFeature {

    private static final Dependency PYRONAUT_PYTEST = Dependency.builder()
            .groupId("io.micronaut.pyronaut")
            .artifactId("micronaut-pyronaut-pytest")
            .test()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "pytest";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        generatorContext.addDependency(PYRONAUT_PYTEST);
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.PYTEST;
    }
}
