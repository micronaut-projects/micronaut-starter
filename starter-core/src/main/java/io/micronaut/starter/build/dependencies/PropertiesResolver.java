/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Optional;

public interface PropertiesResolver {
    String PROPERTY_PREFIX = "${";
    String PROPERTY_SUFFIX = ".version}";
    String CLOSE_BRACKET = "}";

    Optional<String> resolve(@NonNull String key);

    default Optional<String> getPropertyKey(@Nullable String version) {
        if (version != null &&
                version.startsWith(PROPERTY_PREFIX) &&
                version.endsWith(PROPERTY_SUFFIX)) {
            return Optional.of(version.substring(version.indexOf(PROPERTY_PREFIX) + PROPERTY_PREFIX.length(), version.indexOf(CLOSE_BRACKET)));
        }
        return Optional.empty();
    }
}
