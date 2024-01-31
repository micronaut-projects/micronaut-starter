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
package io.micronaut.starter.feature.function.gcp;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.BuildTool;

public final class GcpCloudFunctionBuildCommandUtils {

    public static final String MAVEN_PACKAGE_COMMAND = "mvnw clean package";
    public static final String GRADLE_PACKAGE_COMMAND = "gradlew shadowJar";

    private GcpCloudFunctionBuildCommandUtils() {
    }

    @NonNull
    public static String getBuildCommand(@NonNull BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return MAVEN_PACKAGE_COMMAND;
        } else {
            return GRADLE_PACKAGE_COMMAND;
        }
    }

    @NonNull
    public static String getRunCommand(@NonNull BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw function:run";
        } else {
            return "gradlew runFunction";
        }
    }
}
