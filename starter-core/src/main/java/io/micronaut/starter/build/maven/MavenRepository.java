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
package io.micronaut.starter.build.maven;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.feature.build.maven.templates.repository;
import java.util.List;
import java.util.stream.Collectors;

public class MavenRepository extends RockerWritable {

    public MavenRepository(RockerModel model) {
        super(model);
    }

    public MavenRepository(String id, String url, boolean snapshot) {
        this(repository.template(id, url, snapshot));
    }

    @NonNull
    public static List<MavenRepository> listOf(List<Repository> repositories) {
        return repositories.stream()
                .map(it -> new MavenRepository(it.getId(), it.getUrl(), it.isSnapshot()))
                .collect(Collectors.toList());
    }
}
