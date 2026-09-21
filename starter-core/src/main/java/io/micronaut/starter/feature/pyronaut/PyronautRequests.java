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
package io.micronaut.starter.feature.pyronaut;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.PythonSpecificFeature;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Singleton
public class PyronautRequests implements PythonSpecificFeature, DefaultFeature {
    private static final Dependency DEPENDENCY_TEST_PYRONAUT_REQUESTS =
            MicronautDependencyUtils.pyronautDependency()
                    .artifactId("micronaut-pyronaut-requests")
                    .test()
                    .build();

    @Override
    public @NonNull String getName() {
        return "pyronaut-requests";
    }

    @Override
    public @Nullable String getTitle() {
        return "Pyronaut Requests";
    }

    @Override
    public @Nullable String getDescription() {
        return "drop-in compatible replacement for the Python requests library that runs on GraalPy and delegates HTTP operations to the Micronaut HTTP Client.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_TEST_PYRONAUT_REQUESTS);
    }

    @Override
    public String getCategory() {
        return Category.TEST;
    }
}
