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
import io.micronaut.starter.template.WritableUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class GradleRepository implements Writable  {
    private final GradleDsl gradleDsl;
    private final String url;
    private final boolean isSnapshot;

    public GradleRepository(GradleDsl gradleDsl, String url) {
        this(gradleDsl, url, false);
    }

    public GradleRepository(GradleDsl gradleDsl, String url, boolean isSnapshot) {
        this.gradleDsl = gradleDsl;
        this.url = url;
        this.isSnapshot = isSnapshot;
    }

    public String getUrl() {
        return url;
    }

    public GradleDsl getGradleDsl() {
        return gradleDsl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GradleRepository that = (GradleRepository) o;

        if (isSnapshot != that.isSnapshot) {
            return false;
        }
        if (gradleDsl != that.gradleDsl) {
            return false;
        }
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = gradleDsl != null ? gradleDsl.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (isSnapshot ? 1 : 0);
        return result;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        String content = getGradleDsl() == GradleDsl.KOTLIN ? kotlinDslRepository() : groovyDslRepository();
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
    }

    private String kotlinDslRepository() {
        return "maven(\"" + getUrl() + "\")" + (isSnapshot ? " {\n    mavenContent { snapshotsOnly() }\n}" : "") + "\n";
    }

    private String groovyDslRepository() {
        return "maven { url \"" + getUrl() + "\"" + (isSnapshot ? "\n    mavenContent { snapshotsOnly() }\n" : " ") + "}\n";
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
                    return of(gradleDsl, it);
                }).collect(Collectors.toList());
    }

    @NonNull
    public static GradleRepository of(GradleDsl gradleDsl, Repository repository) {
        return new GradleRepository(gradleDsl, repository.getUrl(), repository.isSnapshot());
    }

    public String render(int indentationSpaces) {
        return WritableUtils.renderWritable(this, indentationSpaces);
    }
}
