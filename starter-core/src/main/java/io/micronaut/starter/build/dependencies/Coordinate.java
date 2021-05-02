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
package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.order.OrderUtil;

import java.util.Comparator;

@Introspected
public interface Coordinate {

    Comparator<Coordinate> COMPARATOR = (o1, o2) -> {
        int comparison = OrderUtil.COMPARATOR.compare(o1, o2);
        if (comparison != 0) {
            return comparison;
        }
        comparison = o1.getGroupId().compareTo(o2.getGroupId());
        if (comparison != 0) {
            return comparison;
        }
        return o1.getArtifactId().compareTo(o2.getArtifactId());
    };

    @NonNull
    String getGroupId();

    @NonNull
    String getArtifactId();

    @Nullable
    default String getVersion() {
        return null;
    }
}
