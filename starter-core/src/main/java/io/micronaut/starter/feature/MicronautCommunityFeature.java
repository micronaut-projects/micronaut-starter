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
package io.micronaut.starter.feature;

import io.micronaut.starter.options.MicronautVersion;
import io.micronaut.starter.util.VersionInfo;

/**
 * Markert interface for {@link CommunityFeature} which expose a Micronaut Library.
 */
public interface MicronautCommunityFeature extends CommunityFeature {
    MicronautVersion builtWithMicronautVersion();

    @Override
    default boolean isVisible() {
        return supportsCurrentMicronautVersion();
    }

    default boolean supportsCurrentMicronautVersion() {
        return VersionInfo.getMicronautMajorVersion()
                .filter(currentMicronautVersion -> builtWithMicronautVersion().getMajor() == currentMicronautVersion)
                .isPresent();
    }
}
