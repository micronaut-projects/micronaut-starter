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
package io.micronaut.starter.build.gradle;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GradleBuild {
    private static final Logger LOG = LoggerFactory.getLogger(GradleBuild.class);

    @NonNull
    private final GradleDsl dsl;

    @NonNull
    private final List<GradleDependency> dependencies;

    @NonNull
    private final List<? extends GradlePlugin> plugins;

    public GradleBuild() {
        this(GradleDsl.GROOVY, Collections.emptyList());
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies) {
        this(gradleDsl, dependencies, Collections.emptyList());
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies,
                       @NonNull List<? extends GradlePlugin> plugins) {
        this.dsl = gradleDsl;
        this.dependencies = dependencies;
        this.plugins = plugins;
    }

    @NonNull
    public GradleDsl getDsl() {
        return dsl;
    }

    @NonNull
    public List<GradleDependency> getDependencies() {
        return dependencies;
    }

    @NonNull
    public List<CoreGradlePlugin> getCorePlugins() {
        return plugins
                .stream()
                .filter(gradlePlugin -> gradlePlugin instanceof CoreGradlePlugin)
                .map(gradlePlugin -> ((CoreGradlePlugin) gradlePlugin))
                .collect(Collectors.toList());
    }

    @NonNull
    public List<CommunityGradlePlugin> getCommunityPlugins() {
        return plugins
                .stream()
                .filter(gradlePlugin -> gradlePlugin instanceof CommunityGradlePlugin)
                .map(gradlePlugin -> ((CommunityGradlePlugin) gradlePlugin))
                .collect(Collectors.toList());
    }

    @NonNull
    public String renderExtensions() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        plugins.stream()
                .filter(gradlePlugin -> gradlePlugin instanceof CommunityGradlePlugin)
                .map(gradlePlugin -> ((CommunityGradlePlugin) gradlePlugin).getExtension())
                .filter(Objects::nonNull)
                .forEach(writable -> {
                    try {
                        writable.write(outputStream);
                    } catch (IOException e) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("IO Exception rendering Gradle Plugin extension");
                        }
                    }
                });
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
}
