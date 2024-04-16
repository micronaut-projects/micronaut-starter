/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.testcontainers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.dependencies.Dependency;

import java.util.List;

public interface ContributingTestContainerDependency {
    String TESTCONTAINERS_GROUP_ID = "org.testcontainers";

    List<Dependency> testContainersDependencies();

    @NonNull
    static Dependency testContainerDependency(@NonNull String artifactId) {
        return Dependency.builder()
                .groupId(TESTCONTAINERS_GROUP_ID)
                .artifactId(artifactId)
                .test()
                .build();
    }
}
