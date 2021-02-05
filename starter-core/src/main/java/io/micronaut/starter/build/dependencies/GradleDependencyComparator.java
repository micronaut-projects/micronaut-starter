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
package io.micronaut.starter.build.dependencies;

import java.util.Comparator;

public class GradleDependencyComparator implements Comparator<Dependency> {
    private final MavenCoordinateComparator mavenCoordinateComparator;

    public GradleDependencyComparator() {
        this.mavenCoordinateComparator = new MavenCoordinateComparator();
    }

    @Override
    public int compare(Dependency o1, Dependency o2) {
        int comparison = 0;
        if (o1 instanceof GradleDependency && o2 instanceof GradleDependency) {
            comparison = Integer.compare(((GradleDependency) o1).getConfiguration().getOrder(), ((GradleDependency) o2).getConfiguration().getOrder());
        }
        if (comparison != 0) {
            return comparison;
        }
        return mavenCoordinateComparator.compare(o1, o2);
    }
}
