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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.Nullable;

public interface GradleEnterpriseConfiguration {
    @Nullable
    default String getServer() {
        return null;
    }

    @Nullable
    default Boolean allowTrustedServer() {
        return null;
    }

    @Nullable
    default String getTermsOfServiceUrl() {
        return "https://gradle.com/terms-of-service";
    }

    /**
     * @deprecated Use {@link GradleEnterpriseConfiguration#aggreeWithTermsOfService()}
     * @return Whether terms of service should automatically agreed.
     */
    @Deprecated
    @Nullable
    default String getTermsOfServiceAgree() {
        return "true";
    }

    default Boolean aggreeWithTermsOfService() {
        return true;
    }
}
