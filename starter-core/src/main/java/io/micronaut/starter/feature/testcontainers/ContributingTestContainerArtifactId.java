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

import io.micronaut.starter.build.dependencies.Dependency;
import java.util.Collections;
import java.util.List;

/**
 * API to flag a feature as contributing a Test Container Dependency.
 * By implementing this instead of {@link ContributingTestContainerDependency} they only have to provide the artifactId.
 * @author Sergio del Amo
 * @since 4.4.0
 */
public interface ContributingTestContainerArtifactId extends ContributingTestContainerDependency {
    String testContainersArtifactId();

    @Override
    default List<Dependency> testContainersDependencies() {
        return Collections.singletonList(ContributingTestContainerDependency.testContainerDependency(testContainersArtifactId()));
    }
}
