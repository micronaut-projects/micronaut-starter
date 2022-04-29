/*
 * Copyright 2017-2020 original authors
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
import io.micronaut.starter.template.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public GradleBuild() {
        this(GradleDsl.GROOVY, Collections.emptyList());
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies) {
        this(gradleDsl, dependencies, Collections.emptyList());
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies,
                       @NonNull List<GradlePlugin> plugins) {
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
    public List<GradlePlugin> getPlugins() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.BUILD).collect(Collectors.toList());
    }

    @NonNull
    public List<GradlePlugin> getSettingsPlugins() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.SETTINGS).collect(Collectors.toList());
    }

    @NonNull
    public String renderExtensions() {
        List<Writable> extensions = new ArrayList<>();
        plugins.stream()
                .map(GradlePlugin::getExtensions)
                .filter(Objects::nonNull)
                .forEach(extensions::addAll);
        return renderWritableExtensions(extensions.stream());
    }

    @NonNull
    public String renderSettingsExtensions() {
        return renderWritableExtensions(plugins.stream().map(GradlePlugin::getSettingsExtension));
    }

    @NonNull
    public String renderSettingsPluginsManagement() {
        return plugins == null ? "" : plugins.stream()
                .map(plugin -> plugin.getSettingsPluginsManagement()
                            .map(writable -> renderWritableExtensions(Stream.of(writable))).orElse(null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");
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
