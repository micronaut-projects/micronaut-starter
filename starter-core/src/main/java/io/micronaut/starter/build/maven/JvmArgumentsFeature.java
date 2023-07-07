/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.feature.Feature;

import java.util.Collection;
import java.util.List;

/**
 * API for a {@link io.micronaut.starter.feature.Feature} which contributes additional arguments that will be passed to the JVM process, such as Java agent properties.
 * @see <a href="https://micronaut-projects.github.io/micronaut-maven-plugin/latest/run-mojo.html#jvmArguments"></a>
 */
public interface JvmArgumentsFeature {

    @NonNull
    List<String> getJvmArguments();

    @Nullable
    static String getJvmArguments(@NonNull Collection<Feature> features) {
        List<String> jvmArgumentsList = features
                .stream()
                .filter(f -> f instanceof JvmArgumentsFeature)
                .map(f -> ((JvmArgumentsFeature) f).getJvmArguments())
                .flatMap(Collection::stream)
                .toList();
        return CollectionUtils.isEmpty(jvmArgumentsList) ? null : String.join(",", jvmArgumentsList);
    }
}
