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
package io.micronaut.starter.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.MavenCentral;
import io.micronaut.starter.build.MavenLocal;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.template.Writable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class GradleRepository implements Writable  {
    private final GradleDsl gradleDsl;
    private final String url;

    public GradleRepository(GradleDsl gradleDsl, String url) {
        this.gradleDsl = gradleDsl;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public GradleDsl getGradleDsl() {
        return gradleDsl;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        String content = (getGradleDsl() == GradleDsl.KOTLIN ?
                "maven(\"" + getUrl() + "\")" :
                "maven { url \"" + getUrl() + "\" }") + "\n";
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
    }

    @NonNull
    public static List<GradleRepository> listOf(GradleDsl gradleDsl, List<Repository> repositories) {
        return repositories.stream()
                .map(it -> {
                    if (it instanceof MavenCentral) {
                        return new GradleMavenCentral(gradleDsl, it.getUrl());
                    }
                    if (it instanceof MavenLocal) {
                        return new GradleMavenLocal(gradleDsl, it.getUrl());
                    }
                    return new GradleRepository(gradleDsl, it.getUrl());
                }).collect(Collectors.toList());
    }
}
