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

public class MicronautSnapshotRepository implements Repository {

    private static final String SONATYPE_OSS_SNAPSHOT = "https://s01.oss.sonatype.org/content/repositories/snapshots/";

    @Override
    @NonNull
    public String getUrl() {
        return SONATYPE_OSS_SNAPSHOT;
    }

    @Override
    @NonNull
    public String getId() {
        return "sonatype-snapshots";
    }

    @Override
    public boolean isSnapshot() {
        return true;
    }
}
