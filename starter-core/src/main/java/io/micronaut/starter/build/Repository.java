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
package io.micronaut.starter.build;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.util.VersionInfo;

import java.util.ArrayList;
import java.util.List;

public interface Repository {

    @NonNull
    String getId();

    @NonNull
    String getUrl();

    default boolean isSnapshot() {
        return false;
    }

    static List<Repository> micronautRepositories() {
        List<Repository> result = new ArrayList<>();
        result.add(new MavenCentral());
        if (VersionInfo.isMicronautSnapshot()) {
            result.add(new MicronautSnapshotRepository());
        }
        return result;
    }
}
