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

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collections;
import java.util.List;

public class GradleBuild {
    @NonNull
    private final GradleDsl dsl;

    @NonNull
    private final List<GradleDependency> dependencies;

    @NonNull
    private final List<GradlePlugin> plugins;

    public GradleBuild() {
        this(GradleDsl.GROOVY, Collections.emptyList(), Collections.emptyList());
    }

    public GradleBuild(@NonNull GradleDsl gradleDsl,
                       @NonNull List<GradleDependency> dependencies,
                       @NonNull List<GradlePlugin> plugins) {
        this.dsl = gradleDsl;
        this.dependencies = dependencies;
        this.plugins = plugins;
    }

    @NonNull
    public GradleDsl getDsl() {
        return dsl;
    }

    @NonNull
    public List<GradleDependency> getDependencies() {
        return dependencies;
    }

    @NonNull
    public List<GradlePlugin> getPlugins() {
        return plugins;
    }
}
