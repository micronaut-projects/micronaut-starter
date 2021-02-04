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
import io.micronaut.starter.options.BuildTool;

public final class ScopeResolver {

    private ScopeResolver() {

    }

    @NonNull
    static String resolve(GradleConfiguration configuration, BuildTool buildTool) {
        if (buildTool.isGradle()) {
            return configuration.getConfigurationName();
        }
        switch (configuration) {
            case COMPILE_ONLY:
            case COMPILE_ONLY_API:
                return MavenScope.PROVIDED.toString();

            case RUNTIME_ONLY:
                return MavenScope.RUNTIME.toString();

            case TEST_IMPLEMENTATION:
            case TEST_COMPILE_ONLY:
            case TEST_RUNTIME_ONLY:
                return MavenScope.TEST.toString();

            case API:
            case IMPLEMENTATION:
            default:
                return MavenScope.COMPILE.toString();
        }
    }

    @NonNull
    static String resolve(MavenScope scope, BuildTool buildTool) {
        if (buildTool.isGradle()) {
            switch (scope) {
                case COMPILE:
                    return GradleConfiguration.IMPLEMENTATION.getConfigurationName();

                case PROVIDED:
                    return GradleConfiguration.COMPILE_ONLY.getConfigurationName();

                case RUNTIME:
                    return GradleConfiguration.RUNTIME_ONLY.getConfigurationName();

                case TEST:
                    return GradleConfiguration.TEST_IMPLEMENTATION.getConfigurationName();

                case SYSTEM:
                case IMPORT:
                default:
                    return scope.toString();
            }
        }
        return scope.toString();
    }
}
