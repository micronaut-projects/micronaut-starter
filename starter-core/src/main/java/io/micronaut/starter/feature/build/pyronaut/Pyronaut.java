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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.MavenCentral;
import io.micronaut.starter.build.MavenLocal;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.micrometer.CloudWatch;
import io.micronaut.starter.feature.micrometer.Core;
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryFeature;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.feature.security.SecurityOAuth2;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.StringTemplate;
import io.micronaut.starter.template.StringWritable;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Requires(property = "micronaut.starter.feature.pyronaut.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Pyronaut implements BuildFeature {

    private static final String GROUP_ID_MICRONAUT = "io.micronaut";
    private static final String GROUP_ID_SERDE = "io.micronaut.serde";
    private static final String GROUP_ID_PYRONAUT = "io.micronaut.pyronaut";

    private static final Dependency HTTP_SERVER_NETTY = Dependency.builder()
            .groupId(GROUP_ID_MICRONAUT)
            .artifactId("micronaut-http-server-netty")
            .runtime()
            .build();
    private static final Dependency JSON_CORE = Dependency.builder()
            .groupId(GROUP_ID_MICRONAUT)
            .artifactId("micronaut-json-core")
            .runtime()
            .build();
    private static final Dependency SERDE_API = Dependency.builder()
            .groupId(GROUP_ID_SERDE)
            .artifactId("micronaut-serde-api")
            .runtime()
            .build();
    private static final Dependency SERDE_JACKSON = Dependency.builder()
            .groupId(GROUP_ID_SERDE)
            .artifactId("micronaut-serde-jackson")
            .runtime()
            .build();
    private static final Dependency SERDE_PROCESSOR = Dependency.builder()
            .groupId(GROUP_ID_SERDE)
            .artifactId("micronaut-serde-processor")
            .annotationProcessor()
            .build();
    private static final Dependency PYRONAUT_LOGBACK = Dependency.builder()
            .groupId(GROUP_ID_PYRONAUT)
            .artifactId("micronaut-pyronaut-logback")
            .runtime()
            .build();
    private static final Dependency PYRONAUT_PYTEST = Dependency.builder()
            .groupId(GROUP_ID_PYRONAUT)
            .artifactId("micronaut-pyronaut-pytest")
            .test()
            .build();
    private static final Dependency PYRONAUT_REQUESTS = Dependency.builder()
            .groupId(GROUP_ID_PYRONAUT)
            .artifactId("micronaut-pyronaut-requests")
            .test()
            .build();

    private final RepositoryResolver repositoryResolver;

    public Pyronaut(RepositoryResolver repositoryResolver) {
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "pyronaut";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDefaultDependencies(generatorContext);
        generatorContext.getConfiguration().addNested("micronaut.executors.blocking.type", "CACHED");
        generatorContext.getConfiguration().addNested("micronaut.executors.blocking.virtual", false);
        generatorContext.addTemplate("pyprojectToml", new StringTemplate(Template.ROOT, "pyproject.toml", pyprojectToml(generatorContext)));
        generatorContext.addTemplate("pyronautGitignore", new StringTemplate(Template.ROOT, ".gitignore", gitignore()));
        generatorContext.addHelpTemplate(new StringWritable(readme(generatorContext)));
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool() == BuildTool.PYRONAUT;
    }

    private static void addDefaultDependencies(GeneratorContext generatorContext) {
        addDependencyIfMissing(generatorContext, HTTP_SERVER_NETTY);
        addDependencyIfMissing(generatorContext, JSON_CORE);
        addDependencyIfMissing(generatorContext, SERDE_API);
        addDependencyIfMissing(generatorContext, SERDE_JACKSON);
        addDependencyIfMissing(generatorContext, SERDE_PROCESSOR);
        addDependencyIfMissing(generatorContext, PYRONAUT_LOGBACK);
        addDependencyIfMissing(generatorContext, PYRONAUT_PYTEST);
        addDependencyIfMissing(generatorContext, PYRONAUT_REQUESTS);
    }

    private static void addDependencyIfMissing(GeneratorContext generatorContext, Dependency dependency) {
        if (!generatorContext.hasDependency(dependency.getGroupId(), dependency.getArtifactId())) {
            generatorContext.addDependency(dependency);
        }
    }

    private String pyprojectToml(GeneratorContext generatorContext) {
        Map<String, List<String>> dependencies = dependencies(generatorContext);
        List<String> repositories = repositories(generatorContext);
        List<String> additionalModules = testResourcesAdditionalModules(generatorContext);

        StringBuilder builder = new StringBuilder();
        builder.append("[project]\n");
        appendKeyValue(builder, "name", generatorContext.getProject().getName());
        appendKeyValue(builder, "version", "1.0.0");
        appendArray(builder, "dynamic", List.of("scripts"));
        builder.append('\n');

        builder.append("[build-system]\n");
        appendArray(builder, "requires", List.of("setuptools", "wheel", "tomli"));
        appendKeyValue(builder, "build-backend", "setuptools.build_meta");
        builder.append('\n');

        builder.append("[tool.setuptools]\n");
        builder.append("package-dir = {\"\" = \"src\"}\n\n");
        builder.append("[tool.setuptools.packages.find]\n");
        appendArray(builder, "where", List.of("src"));
        builder.append('\n');

        builder.append("[tool.pyronaut]\n");
        appendArray(builder, "repositories", repositories);
        builder.append('\n');

        builder.append("[tool.pyronaut.core]\n");
        appendKeyValue(builder, "version", VersionInfo.getMicronautCoreVersion());
        builder.append('\n');

        builder.append("[tool.pyronaut.platform]\n");
        appendKeyValue(builder, "version", VersionInfo.getMicronautVersion());
        builder.append('\n');

        builder.append("[tool.pyronaut.toolchain]\n");
        appendKeyValue(builder, "type", "jvm");
        builder.append('\n');

        builder.append("[tool.pyronaut.sources]\n");
        appendKeyValue(builder, "python", "src");
        appendKeyValue(builder, "python-test", "tests");
        appendKeyValue(builder, "resources", "config");
        appendKeyValue(builder, "test-resources", "tests-config");
        builder.append('\n');

        builder.append("[tool.pyronaut.processor]\n");
        builder.append("incremental = true\n");
        builder.append("daemon = true\n");
        appendKeyValue(builder, "python-incremental-mode", "optimistic");
        builder.append('\n');

        builder.append("[tool.pyronaut.ide-stubs]\n");
        appendArray(builder, "packages", List.of("io.micronaut", "jakarta"));
        builder.append('\n');

        builder.append("[tool.pyronaut.validation]\n");
        builder.append("enabled = true\n");
        List<String> suppressions = validationSuppressions(generatorContext);
        if (!suppressions.isEmpty()) {
            appendArray(builder, "suppressions", suppressions);
        }
        builder.append('\n');

        builder.append("[tool.pyronaut.test-resources]\n");
        if (generatorContext.hasFeature(TestResources.class)) {
            builder.append("enabled = true\n");
            builder.append("infer-classpath = true\n");
            if (!additionalModules.isEmpty()) {
                appendArray(builder, "additional-modules", additionalModules);
            }
        } else {
            builder.append("enabled = false\n");
            builder.append("infer-classpath = false\n");
        }
        builder.append('\n');

        builder.append("[tool.pyronaut.dependencies]\n");
        appendMultilineArray(builder, "runtime", dependencies.get("runtime"));
        appendMultilineArray(builder, "build", dependencies.get("build"));
        appendMultilineArray(builder, "test", dependencies.get("test"));
        return builder.toString();
    }

    private static List<String> validationSuppressions(GeneratorContext generatorContext) {
        List<String> suppressions = new ArrayList<>();
        if (generatorContext.hasFeature(TestResources.class)) {
            suppressions.add("micronaut.test-resources*");
        }
        if (generatorContext.hasFeature(TestResources.class)
                && generatorContext.hasFeature(R2dbcFeature.class)
                && generatorContext.hasFeature(MigrationFeature.class)) {
            suppressions.add("datasources.*.dialect");
        }
        if (generatorContext.hasFeature(TestResources.class) && generatorContext.hasFeature(R2dbcFeature.class)) {
            suppressions.add("r2dbc.datasources.*");
        }
        if (generatorContext.hasFeature(Management.class)) {
            suppressions.add("endpoints.*.enabled");
            suppressions.add("endpoints.*.sensitive");
        }
        if (generatorContext.hasFeature(Core.class)) {
            suppressions.add("micronaut.metrics.enabled");
            suppressions.add("micronaut.metrics.binders.*");
        }
        if (generatorContext.getFeatures().hasFeature(AwsCloudFeature.class) || generatorContext.hasFeature(CloudWatch.class)) {
            suppressions.add("aws.*");
        }
        if (generatorContext.hasFeature(OpenTelemetryFeature.class)) {
            suppressions.add("otel.*");
        }
        if (generatorContext.hasFeature(SecurityOAuth2.class)) {
            suppressions.add("micronaut.security.oauth2.clients.*.token.auth-method");
        }
        return suppressions;
    }

    private List<String> repositories(GeneratorContext generatorContext) {
        return repositoryResolver.resolveRepositories(generatorContext)
                .stream()
                .map(Pyronaut::repository)
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .toList();
    }

    private static String repository(Repository repository) {
        if (repository instanceof MavenLocal) {
            return null;
        }
        if (repository instanceof MavenCentral) {
            return "mavenCentral";
        }
        return repository.getUrl();
    }

    private static Map<String, List<String>> dependencies(GeneratorContext generatorContext) {
        Map<String, Set<String>> result = new LinkedHashMap<>();
        result.put("runtime", new LinkedHashSet<>());
        result.put("build", new LinkedHashSet<>());
        result.put("test", new LinkedHashSet<>());

        Collection<Dependency> dependencies = generatorContext.removeDuplicates(
                generatorContext.getDependencies(),
                generatorContext.getLanguage(),
                generatorContext.getBuildTool());
        dependencies.stream()
                .filter(dependency -> dependency.getScope() != null)
                .filter(dependency -> StringUtils.isNotEmpty(dependency.getGroupId()))
                .filter(dependency -> !dependency.isPom())
                .sorted(Dependency.COMPARATOR)
                .forEach(dependency -> scopeName(dependency.getScope())
                        .ifPresent(scope -> result.get(scope).add(coordinate(dependency))));

        return Map.of(
                "runtime", sorted(result.get("runtime")),
                "build", sorted(result.get("build")),
                "test", sorted(result.get("test")));
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

    private static List<String> sorted(Collection<String> values) {
        return values.stream().sorted(Comparator.naturalOrder()).toList();
    }

    private static List<String> testResourcesAdditionalModules(GeneratorContext generatorContext) {
        if (!generatorContext.hasFeature(TestResources.class)) {
            return List.of();
        }
        return generatorContext.getFeatures().getFeatures()
                .stream()
                .filter(TestResourcesAdditionalModulesProvider.class::isInstance)
                .map(TestResourcesAdditionalModulesProvider.class::cast)
                .flatMap(feature -> feature.getTestResourcesAdditionalModules(generatorContext).stream())
                .distinct()
                .sorted()
                .toList();
    }

    private static void appendKeyValue(StringBuilder builder, String key, String value) {
        builder.append(key).append(" = ");
        appendQuoted(builder, value);
        builder.append('\n');
    }

    private static void appendArray(StringBuilder builder, String key, List<String> values) {
        builder.append(key).append(" = [");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            appendQuoted(builder, values.get(i));
        }
        builder.append("]\n");
    }

    private static void appendMultilineArray(StringBuilder builder, String key, List<String> values) {
        builder.append(key).append(" = [");
        if (!values.isEmpty()) {
            builder.append('\n');
            for (String value : values) {
                builder.append("    ");
                appendQuoted(builder, value);
                builder.append(",\n");
            }
        }
        builder.append("]\n");
    }

    private static void appendQuoted(StringBuilder builder, String value) {
        builder.append('"').append(value.replace("\\", "\\\\").replace("\"", "\\\"")).append('"');
    }

    private static String gitignore() {
        return """
            # Python
            __pycache__/
            *.py[cod]
            *.egg-info/
            .pytest_cache/
            .venv/
            venv/

            # Pyronaut
            __pyronaut__/
            dist/
            build/
            logs/

            # Tooling
            .idea/
            .DS_Store
            """;
    }

    private static String readme(GeneratorContext generatorContext) {
        String projectName = generatorContext.getProject().getName();
        return """

            ## Pyronaut

            This project uses Python with the Pyronaut build tool.

            Requirements:

            * A GraalPy runtime with virtual environment support.
            * A GraalVM JDK supported by Pyronaut for `install`, `process`, `run`, and `test`.
            * The `pyronaut` CLI installed in the active environment or available on `PATH`.

            Create and activate a virtual environment:

            ```bash
            graalpy -m venv .venv
            source .venv/bin/activate
            python -m pip install --upgrade pip pytest
            ```

            Verify the CLI and install project dependencies:

            ```bash
            pyronaut --version
            pyronaut install
            ```

            `pyronaut install` also writes the local TOML schema files and schema directives used by IDEs.

            Process the application:

            ```bash
            pyronaut process
            ```

            Run the application:

            ```bash
            pyronaut run
            ```

            Run tests:

            ```bash
            pyronaut test
            ```

            The application configuration is in `config/application.toml`; test-only configuration belongs in `tests-config/`.
            Project metadata and Pyronaut dependencies are declared in `pyproject.toml` for `%s`.
            """.formatted(projectName);
    }
}
