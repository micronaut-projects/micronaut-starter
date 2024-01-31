/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.core.annotation.NonNull;

import java.util.Map;

public record InterceptUrlMap(@NonNull String pattern, @NonNull String access) {
    private static final String IS_ANONYMOUS = "isAnonymous()";

    public static InterceptUrlMap anonymousAcccess(@NonNull String pattern) {
        return new InterceptUrlMap(pattern, IS_ANONYMOUS);
    }

    public Map<String, String> toMap() {
        return Map.of("pattern", pattern, "access", access);
    }
}
