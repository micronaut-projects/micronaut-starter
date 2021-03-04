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

import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.build.maven.MavenCoordinateComparator;
import java.util.Comparator;

public class GradleDependencyComparator implements Comparator<GradleDependency> {
    private final MavenCoordinateComparator mavenCoordinateComparator;

    public GradleDependencyComparator() {
        this.mavenCoordinateComparator = new MavenCoordinateComparator();
    }

    @Override
    public int compare(GradleDependency o1, GradleDependency o2) {
        int comparison = OrderUtil.COMPARATOR.compare(o1, o2);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(o1.getConfiguration().getOrder(), o2.getConfiguration().getOrder());
        if (comparison != 0) {
            return comparison;
        }
        return mavenCoordinateComparator.compare(o1, o2);
    }
}
