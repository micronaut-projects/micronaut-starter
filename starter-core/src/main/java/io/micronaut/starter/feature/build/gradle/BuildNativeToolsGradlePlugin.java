/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BuildNativeToolsGradlePlugin {
    private Boolean toolchainDetection;
    private List<String> buildArgs;

    public BuildNativeToolsGradlePlugin(Boolean toolchainDetection,
                                        List<String> buildArgs) {
        this.toolchainDetection = toolchainDetection;
        this.buildArgs = buildArgs;
    }

    public Boolean getToolchainDetection() {
        return toolchainDetection;
    }

    public List<String> getBuildArgs() {
        return buildArgs;
    }

    @NonNull
    public static BuildNativeToolsGradlePlugin.Builder builder() {
        return new BuildNativeToolsGradlePlugin.Builder();
    }

    public boolean isEmpty() {
        return getToolchainDetection() == null
                && CollectionUtils.isEmpty(getBuildArgs());
    }

    public static final class Builder {
        private Boolean toolchainDetection;
        private List<String> buildArgs;

        public Builder toolchainDetection(Boolean toolchainDetection) {
            this.toolchainDetection = toolchainDetection;
            return this;
        }

        public Builder buildArg(String arg) {
            if (this.buildArgs == null) {
                this.buildArgs = new ArrayList<>();
            }
            this.buildArgs.add(arg);
            return this;
        }

        public BuildNativeToolsGradlePlugin build() {
            return new BuildNativeToolsGradlePlugin(toolchainDetection, buildArgs);
        }
    }
}
