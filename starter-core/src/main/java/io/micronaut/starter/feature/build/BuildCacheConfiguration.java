/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.build;

import io.micronaut.core.annotation.Nullable;

public interface BuildCacheConfiguration {
    @Nullable
    default boolean isUseCustomCachePlugin() {
        return false;
    }

    @Nullable
    default boolean isEnableLocalCache() {
        return true;
    }

    @Nullable
    default String getLocalCacheDir() {
        return null;
    }

    @Nullable
    default Integer getLocalCacheTimeout() {
        return null;
    }

    @Nullable
    default boolean isEnableRemoteCache() {
        return false;
    }

    @Nullable
    default String getRemoteCacheType() {
        return "HttpBuildCache";
    }

    @Nullable
    default String getRemoteCacheUri() {
        return null;
    }

    @Nullable
    default boolean isUseExpectContinue() {
        return true;
    }

    @Nullable
    default boolean isRemoteCachePushEnabled() {
        return true;
    }

    @Nullable
    default boolean isRemoteCacheAllowUntrustedServer() {
        return false;
    }

    @Nullable
    default boolean isRemoteCacheAllowInsecureProtocol() {
        return false;
    }
}
