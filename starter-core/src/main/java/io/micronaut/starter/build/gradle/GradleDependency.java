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

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import java.util.Comparator;
import java.util.Objects;

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

    @NonNull
    private final GradleConfiguration gradleConfiguration;

    public GradleDependency(@NonNull Dependency dependency,
                            @NonNull GeneratorContext generatorContext) {
        super(dependency);
        gradleConfiguration = GradleConfiguration.of(
                dependency.getScope(),
                generatorContext.getLanguage(),
                generatorContext.getTestFramework()
        ).orElseThrow(() ->
                new IllegalArgumentException(String.format("Cannot map the dependency scope: [%s] to a Gradle specific scope", dependency.getScope())));
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
            snippet += " platform";
        }
        snippet += "(\"" + getGroupId() + ':' + getArtifactId() +
                (getVersion() != null ? (':' + getVersion()) : "") + "\")";
        return snippet;
    }
}
