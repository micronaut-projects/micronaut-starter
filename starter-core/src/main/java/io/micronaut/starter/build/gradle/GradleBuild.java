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
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import io.micronaut.starter.build.dependencies.Substitution;
import io.micronaut.starter.feature.build.gradle.templates.settingsPluginManagement;
import io.micronaut.starter.feature.build.gradle.templates.substitutions;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.Writable;
import io.micronaut.starter.template.WritableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GradleBuild {
    private static final Logger LOG = LoggerFactory.getLogger(GradleBuild.class);

    private final GradleDsl dsl;
    private final List<GradleDependency> dependencies;
    private final List<GradlePlugin> plugins;
    private final List<GradleRepository> repositories;

    public GradleBuild() {
        this(GradleDsl.GROOVY, Collections.emptyList(), Collections.emptyList());
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies,
                       @NonNull List<GradleRepository> repositories) {
        this(gradleDsl, dependencies, Collections.emptyList(), repositories);
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies,
                       @NonNull List<GradlePlugin> plugins,
                       @NonNull List<GradleRepository> repositories) {
        this.dsl = gradleDsl;
        this.dependencies = dependencies;
        this.plugins = plugins;
        this.repositories = repositories;
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
    public List<GradlePlugin> getPlugins() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.BUILD).collect(Collectors.toList());
    }

    @NonNull
    public List<String> getSettingsImports() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.SETTINGS).map(GradlePlugin::getSettingsImports).flatMap(Collection::stream).toList();
    }

    @NonNull
    public List<GradlePlugin> getSettingsPlugins() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.SETTINGS).collect(Collectors.toList());
    }

    @NonNull
    public String renderSubstitutions() {
        Set<Substitution> uniqueSubstitutions = new HashSet<>();
        dependencies
                .stream()
                .map(DependencyCoordinate::getSubstitutions)
                .filter(Objects::nonNull)
                .forEach(uniqueSubstitutions::addAll);
        return CollectionUtils.isEmpty(uniqueSubstitutions) ? "" :
                renderWritableExtensions(Stream.of(
                        new RockerWritable(substitutions.template(uniqueSubstitutions))));
    }

    @NonNull
    public String renderExtensions() {
        return renderWritableExtensions(plugins.stream()
                .map(GradlePlugin::getExtension)
                .filter(Objects::nonNull));
    }

    @NonNull
    public String renderSettingsExtensions() {
        return renderWritableExtensions(plugins.stream().map(GradlePlugin::getSettingsExtension));
    }

    @NonNull
    public String renderRepositories() {
        return WritableUtils.renderWritableList(repositories.stream()
                .map(Writable.class::cast).collect(Collectors.toList()), 4);
    }

    @NonNull
    public String renderSettingsPluginsManagement() {
        List<GradleRepository> repos = plugins.stream()
                .flatMap(plugin -> plugin.getPluginsManagementRepositories().stream())
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(repos)) {
            return "";
        }
        return WritableUtils.renderWritable(new RockerWritable(settingsPluginManagement.template(repos)), 0);
    }

    @NonNull
    private String renderWritableExtensions(Stream<Writable> extensions) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        extensions
                .filter(Objects::nonNull)
                .forEach(writable -> {
                    try {
                        writable.write(outputStream);
                        outputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("IO Exception rendering Gradle Plugin extension");
                        }
                    }
                });
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }

    @NonNull
    public Set<String> getPluginsImports() {
        Set<String> imports = new HashSet<>();
        for (GradlePlugin p : plugins) {
            Set<String> pluginImports = p.getBuildImports();
            if (pluginImports != null) {
                imports.addAll(pluginImports);
            }
        }
        return imports.stream().map(it -> it + System.lineSeparator()).collect(Collectors.toSet());
    }
}
