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
import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.*;

import java.util.*;
import static io.micronaut.core.util.CollectionUtils.isNotEmpty;

public class GradleDependency extends DependencyCoordinate {

    public static final Comparator<GradleDependency> COMPARATOR = (o1, o2) -> {
        int comparison = OrderUtil.COMPARATOR.compare(o1, o2);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(o1.getConfiguration().getOrder(), o2.getConfiguration().getOrder());
        if (comparison != 0) {
            return comparison;
        }
        return Coordinate.COMPARATOR.compare(o1, o2);
    };

    private final Boolean isKotlinDSL;

    @NonNull
    private final GradleConfiguration gradleConfiguration;

    private final boolean useVersionCatalogue;

    public GradleDependency(@NonNull Dependency dependency,
                            @NonNull GeneratorContext generatorContext,
                            boolean useVersionCatalogue) {
        super(dependency);
        gradleConfiguration = GradleConfiguration.of(
                dependency.getScope(),
                generatorContext.getLanguage(),
                generatorContext.getTestFramework(),
                generatorContext
        ).orElseThrow(() ->
                new IllegalArgumentException("Cannot map the dependency scope: [%s] to a Gradle specific scope".formatted(dependency.getScope())));
        isKotlinDSL = generatorContext.getBuildTool().getGradleDsl().filter(f -> f == GradleDsl.KOTLIN).isPresent();
        this.useVersionCatalogue = useVersionCatalogue;
    }

    @NonNull
    public GradleConfiguration getConfiguration() {
        return gradleConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GradleDependency that = (GradleDependency) o;

        return Objects.equals(gradleConfiguration, that.gradleConfiguration);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + gradleConfiguration.hashCode();
        return result;
    }

    @NonNull
    public String toSnippet() {
        String snippet = gradleConfiguration.getConfigurationName();
        if (isPom()) {
            String platformPrefix = " ";
            if (isKotlinDSL) {
                platformPrefix = "(";
            }
            snippet += platformPrefix + "platform";
        }
        snippet += "(";
        snippet += useVersionCatalogue ? versionCatalog().orElseGet(this::mavenCoordinate)  : mavenCoordinate();
        snippet += ")";
        if (isPom() && isKotlinDSL) {
            snippet += ")";
        }

        if (isNotEmpty(getExclusions())) {
            snippet += " {\n";
            final String mapAccessor = isKotlinDSL ? " = " : ": ";
            final StringBuilder exclusionBuilder = new StringBuilder();
            for (DependencyCoordinate exclusion : getExclusions()) {
                exclusionBuilder
                        .append("      exclude(group").append(mapAccessor).append("\"").append(exclusion.getGroupId())
                        .append("\", module").append(mapAccessor).append("\"").append(exclusion.getArtifactId()).append("\")\n");
            }
            snippet += exclusionBuilder.toString();
            snippet += "    }";
        }
        return snippet;
    }

    /**
     *
     * @return Maven Coordinate surrounded by double quotes
     */
    @NonNull
    public String mavenCoordinate() {
        return "\"" + getGroupId() + ':' + getArtifactId() +
                (getVersion() != null ? (':' + getVersion()) : "") + "\"";
    }

    @NonNull
    public Optional<String> versionCatalog() {
        if (!getGroupId().startsWith("io.micronaut")) {
            return Optional.empty();
        }
        return Optional.of("mn." + getArtifactId().replace("-", "."));
    }

    @NonNull
    public static List<GradleDependency> listOf(GeneratorContext generatorContext, boolean useVersionCatalogue) {
        return listOf(generatorContext, generatorContext, useVersionCatalogue);
    }

    @NonNull
    public static List<GradleDependency> listOf(DependencyContext dependencyContext, GeneratorContext generatorContext, boolean useVersionCatalogue) {
        return generatorContext.removeDuplicates(dependencyContext.getDependencies(), generatorContext.getLanguage(), generatorContext.getBuildTool())
                .stream()
                .map(dep -> new GradleDependency(dep, generatorContext, useVersionCatalogue))
                .sorted(GradleDependency.COMPARATOR)
                .toList();
    }
}
