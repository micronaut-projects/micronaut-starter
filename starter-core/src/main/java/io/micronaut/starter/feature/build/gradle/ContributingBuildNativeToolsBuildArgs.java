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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.starter.application.generator.GeneratorContext;

import java.util.List;

/**
 * Marker interface for features that contribute build arguments which passed to the native image builder options.
 */
public interface ContributingBuildNativeToolsBuildArgs {
    String H_STATIC_EXECUTABLE_WITH_DYNAMIC_LIB_C = "-H:+StaticExecutableWithDynamicLibC";
    String STRICT_IMAGE_HEAP = "--strict-image-heap";

    List<String> getBuildArgs(GeneratorContext generatorContext);
}
