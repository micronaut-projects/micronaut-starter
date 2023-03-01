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
package io.micronaut.starter.build.dependencies;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public class DefaultPomDependencyVersionResolver implements PomDependencyVersionResolver {
    /**
     * Initialize coordinates early to remove runtime dependencies on javax.xml.
     */
    private static final Map<String, Coordinate> COORDINATES = CoordinatesUtils.ALL_COORDINATES;

    @Override
    @NonNull
    public Optional<Coordinate> resolve(@NonNull String artifactId) {
        return Optional.ofNullable(COORDINATES.get(artifactId));
    }

    @NonNull
    public Map<String, Coordinate> getCoordinates() {
        return COORDINATES;
    }
}
