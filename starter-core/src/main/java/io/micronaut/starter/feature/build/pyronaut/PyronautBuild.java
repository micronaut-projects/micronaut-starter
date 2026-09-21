/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.build.pyronaut;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.config.toml.TomlPath;
import io.micronaut.starter.feature.config.toml.TomlTable;
import io.micronaut.starter.util.VersionInfo;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record PyronautBuild(String projectName,
                            String version,
                            List<String> repositories,
                            Collection<Dependency> dependencies,
                            boolean testResources,
                            List<String> additionalModules,
                            List<String> suppressions) {
    public static final String TABLE_TOOL = "tool";
    public static final String TABLE_PYRONAUT = "pyronaut";
    public static final String TABLE_DEPENDENCIES = "dependencies";

    public String render() {
        StringBuilder builder = new StringBuilder();

        Map<String, Object> values = new LinkedHashMap<>();
        values.put("name", projectName);
        values.put("version", version);
        values.put("dynamic", List.of("scripts"));
        builder.append(new TomlTable(new TomlPath("project"), values));

        builder.append('\n');

        values = new LinkedHashMap<>();
        values.put("requires", List.of("setuptools", "wheel", "tomli"));
        values.put("build-backend", "setuptools.build_meta");
        builder.append(new TomlTable(new TomlPath("build-system"), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("package-dir", Map.of("", "src"));
        builder.append(new TomlTable(new TomlPath(List.of("tool", "setuptools")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("where", List.of("src"));
        builder.append(new TomlTable(new TomlPath(List.of("tool", "setuptools", "packages", "find")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("repositories", repositories);
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("version", VersionInfo.getMicronautCoreVersion());
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "core")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("version", VersionInfo.getMicronautVersion());
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "platform")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("type", "jvm");
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "toolchain")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("python", "src");
        values.put("python-test", "tests");
        values.put("resources", "config");
        values.put("test-resources", "tests-config");
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "sources")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("incremental", true);
        values.put("daemon", true);
        values.put("python-incremental-mode", "optimistic");
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "processor")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("packages", List.of("io.micronaut", "jakarta"));
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "ide-stubs")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("enabled", true);
        if (!suppressions.isEmpty()) {
            values.put("suppressions", suppressions);
        }
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "validation")), values));

        builder.append('\n');
        values = new LinkedHashMap<>();
        values.put("enabled", testResources);
        values.put("infer-classpath", testResources);
        if (testResources && !additionalModules.isEmpty()) {
            values.put("additional-modules", additionalModules);
        }
        builder.append(new TomlTable(new TomlPath(List.of("tool", "pyronaut", "test-resources")), values));

        Map<String, Object> dependencyMap = dependenciesMap(dependencies);
        builder.append('\n');
        builder.append(new TomlTable(new TomlPath(List.of(TABLE_TOOL, TABLE_PYRONAUT, TABLE_DEPENDENCIES)), dependencyMap));
        return builder.toString();
    }

    private static Map<String, Object> dependenciesMap(Collection<Dependency> dependencies) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("runtime", new LinkedHashSet<>());
        result.put("build", new LinkedHashSet<>());
        result.put("test", new LinkedHashSet<>());

        dependencies.stream()
                .filter(dependency -> dependency.getScope() != null)
                .filter(dependency -> StringUtils.isNotEmpty(dependency.getGroupId()))
                .filter(dependency -> !dependency.isPom())
                .sorted(Dependency.COMPARATOR)
                .forEach(dependency -> scopeName(dependency.getScope())
                        .ifPresent(scope -> {
                            if ((result.get(scope) instanceof Set s)) {
                                s.add(coordinate(dependency));
                            }
                        }));
        Object runtimeObject = sorted((Set<String>) result.get("runtime"));
        Object buildObject = sorted((Set<String>) result.get("build"));
        Object testObject = sorted((Set<String>) result.get("test"));
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("runtime", runtimeObject);
        m.put("build", buildObject);
        m.put("test", testObject);
        return m;
    }

    private static List<String> sorted(Collection<String> values) {
        return values.stream().sorted(Comparator.naturalOrder()).toList();
    }

    private static java.util.Optional<String> scopeName(Scope scope) {
        if (scope == Scope.ANNOTATION_PROCESSOR || scope == Scope.COMPILE_ONLY) {
            return java.util.Optional.of("build");
        }
        if (scope == Scope.API || scope == Scope.COMPILE || scope == Scope.RUNTIME || scope == Scope.DEVELOPMENT_ONLY) {
            return java.util.Optional.of("runtime");
        }
        if (scope == Scope.TEST || scope == Scope.TEST_RUNTIME || scope == Scope.TEST_COMPILE_ONLY
                || scope == Scope.TEST_ANNOTATION_PROCESSOR || scope == Scope.TEST_RESOURCES_SERVICE) {
            return java.util.Optional.of("test");
        }
        return java.util.Optional.empty();
    }

    private static String coordinate(Dependency dependency) {
        String version = dependency.getVersion();
        if (StringUtils.isEmpty(version) || version.startsWith("${")) {
            return dependency.getGroupId() + ":" + dependency.getArtifactId();
        }
        return dependency.getGroupId() + ":" + dependency.getArtifactId() + ":" + version;
    }
}
