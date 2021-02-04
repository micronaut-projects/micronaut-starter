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
package io.micronaut.starter.feature.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class GradleDependency {

    @NonNull
    private GradleConfiguration gradleConfiguration;

    @NonNull
    private MavenCoordinate mavenCoordinate;

    public GradleDependency(@NonNull GradleConfiguration gradleConfiguration,
                            @NonNull MavenCoordinate coordinate) {
        this.gradleConfiguration = gradleConfiguration;
        this.mavenCoordinate = coordinate;
    }

    @NonNull
    public GradleConfiguration getGradleConfiguration() {
        return gradleConfiguration;
    }

    public void setGradleConfiguration(@NonNull GradleConfiguration gradleConfiguration) {
        this.gradleConfiguration = gradleConfiguration;
    }

    @NonNull
    public MavenCoordinate getMavenCoordinate() {
        return mavenCoordinate;
    }

    public void setMavenCoordinate(@NonNull MavenCoordinate mavenCoordinate) {
        this.mavenCoordinate = mavenCoordinate;
    }
}
