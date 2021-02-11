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

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.exceptions.ConfigurationException;

import java.util.Optional;

public class MavenDependencyAdapter implements MavenDependency {

    private final ScopedDependency scopedDependency;

    public MavenDependencyAdapter(ScopedDependency scopedDependency) {
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
    public MavenScope getMavenScope() {
        Optional<MavenScope> mavenScope = parse(scopedDependency.getScope());
        if (mavenScope.isPresent()) {
            return mavenScope.get();
        }
        throw new ConfigurationException("Could not parse " + scopedDependency.getScope().toString() + " as a maven scope");
    }

    @NonNull
    private static Optional<MavenScope> parse(@NonNull Scope scope) {
        switch (scope.getSource()) {
            case MAIN:
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION)) {
                        return Optional.of(MavenScope.COMPILE);
                    }
                    return Optional.of(MavenScope.RUNTIME);
                }
                break;

            case TEST:
                return Optional.of(MavenScope.TEST);
            default:
                return Optional.empty();
        }
        return Optional.empty();
    }
}
