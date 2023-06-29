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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.gradle.GradlePlugin;

public class MicronautTestResourcesGradlePlugin {

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        public static final String MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID = "io.micronaut.test-resources";
        public static final String ARTIFACT_ID = "micronaut-test-resources-plugin";

        public GradlePlugin build() {
            return GradlePlugin.builder()
                    .id(MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID)
                    .lookupArtifactId(ARTIFACT_ID)
                    .build();
        }
    }
}
