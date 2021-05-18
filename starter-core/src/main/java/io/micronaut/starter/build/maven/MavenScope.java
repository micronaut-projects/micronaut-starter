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
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.Scope;

import java.util.Optional;

public enum MavenScope implements Ordered {
    COMPILE("compile", 0),
    PROVIDED("provided", 2),
    RUNTIME("runtime", 1),
    TEST("test", 3),
    SYSTEM("system", -1),
    IMPORT("import", -1);

    private final String scope;
    private final int order;

    MavenScope(String scope, int order) {
        this.scope = scope;
        this.order = order;
    }

    @Override
    public String toString() {
        return scope;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @NonNull
    public static Optional<MavenScope> of(@NonNull Scope scope) {
        switch (scope.getSource()) {
            case MAIN:
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION)) {
                        return Optional.of(MavenScope.COMPILE);
                    }
                    return Optional.of(MavenScope.RUNTIME);
                }
                if (scope.getPhases().contains(Phase.COMPILATION)) {
                    return Optional.of(MavenScope.PROVIDED);
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
