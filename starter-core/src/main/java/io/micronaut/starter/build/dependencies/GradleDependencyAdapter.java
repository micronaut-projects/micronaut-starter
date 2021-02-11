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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.context.exceptions.ConfigurationException;
import java.util.Optional;

public class GradleDependencyAdapter implements GradleDependency {

    private final ScopedDependency scopedDependency;

    public GradleDependencyAdapter(ScopedDependency scopedDependency) {
        this.scopedDependency = scopedDependency;
    }

    @NonNull
    @Override
    public String getGroupId() {
        return scopedDependency.getGroupId();
    }

    @NonNull
    @Override
    public String getArtifactId() {
        return scopedDependency.getArtifactId();
    }

    @Nullable
    @Override
    public String getVersion() {
        return scopedDependency.getVersion();
    }

    @NonNull
    @Override
    public GradleConfiguration getConfiguration() {
        Optional<GradleConfiguration> configuration = parse(scopedDependency.getScope());
        if (configuration.isPresent()) {
            return configuration.get();
        }
        throw new ConfigurationException("Could not parse " + scopedDependency.getScope().toString() + " as a gradle configuration");
    }

    @NonNull
    private static Optional<GradleConfiguration> parse(@NonNull Scope scope) {
        switch (scope.getSource()) {
            case MAIN:
                if (scope.getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                    return Optional.of(GradleConfiguration.ANNOTATION_PROCESSOR);
                }
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION)) {
                        return Optional.of(GradleConfiguration.IMPLEMENTATION);
                    }
                    return Optional.of(GradleConfiguration.RUNTIME_ONLY);
                }
                if (scope.getPhases().contains(Phase.COMPILATION)) {
                    return Optional.of(GradleConfiguration.COMPILE_ONLY);
                }

                break;

            case TEST:
                if (scope.getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                    return Optional.of(GradleConfiguration.TEST_ANNOTATION_PROCESSOR);
                }
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION)) {
                        return Optional.of(GradleConfiguration.TEST_IMPLEMENTATION);
                    }
                    return Optional.of(GradleConfiguration.TEST_RUNTIME_ONLY);
                }
                if (scope.getPhases().contains(Phase.COMPILATION)) {
                    return Optional.of(GradleConfiguration.TEST_COMPILE_ONLY);
                }
                break;

            default:
                return Optional.empty();
        }
        return Optional.empty();
    }
}
