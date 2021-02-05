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

public class MavenCoordinateComparator implements Comparator<MavenCoordinate> {
    @Override
    public int compare(MavenCoordinate o1, MavenCoordinate o2) {
        int comparison = o1.getGroupId().compareTo(o2.getGroupId());
        if (comparison != 0) {
            return comparison;
        }
        return o1.getArtifactId().compareTo(o2.getArtifactId());
    }
}
