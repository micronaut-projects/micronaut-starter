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
package io.micronaut.starter.build.maven;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;

import java.util.Comparator;

public class MavenDependency extends DependencyCoordinate {

    public static final Comparator<MavenDependency> COMPARATOR = (o1, o2) -> {
        int comparison = OrderUtil.COMPARATOR.compare(o1, o2);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(o1.getMavenScope().getOrder(), o2.getMavenScope().getOrder());
        if (comparison != 0) {
            return comparison;
        }
        return DependencyCoordinate.COMPARATOR.compare(o1, o2);
    };

    @NonNull
    private final MavenScope mavenScope;

    public MavenDependency(@NonNull Dependency dependency) {
        super(dependency);
        if (isPom()) {
            mavenScope = MavenScope.IMPORT;
        } else {
            mavenScope = MavenScope.of(dependency.getScope()).orElseThrow(() ->
                    new IllegalArgumentException(String.format("Cannot map the dependency scope: [%s] to a Maven specific scope", dependency.getScope())));
        }
    }

    public MavenScope getMavenScope() {
        return mavenScope;
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

        MavenDependency that = (MavenDependency) o;

        return mavenScope == that.mavenScope;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mavenScope.hashCode();
        return result;
    }
}
