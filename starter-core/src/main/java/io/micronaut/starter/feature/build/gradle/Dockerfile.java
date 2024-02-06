/*
 * Copyright 2017-2021 original authors
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
import io.micronaut.core.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Dockerfile {
    @Nullable
    private String baseImage;

    @Nullable
    private String javaVersion;

    @Nullable
    private List<String> args;

    public Dockerfile(@Nullable String baseImage, @Nullable String javaVersion, @Nullable List<String> args) {
        this.baseImage = baseImage;
        this.javaVersion = javaVersion;
        this.args = args;
    }

    @Nullable
    public String getBaseImage() {
        return baseImage;
    }

    @Nullable
    public String getJavaVersion() {
        return javaVersion;
    }

    @Nullable
    public List<String> getArgs() {
        return args;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String baseImage;
        private String javaVersion;
        private List<String> args;

        @NonNull
        public Builder baseImage(String baseImage) {
            this.baseImage = baseImage;
            return this;
        }

        public Builder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        @NonNull
        public Builder arg(String arg) {
            if (args == null) {
                args = new ArrayList<>();
            }
            args.add(arg);
            return this;
        }

        @NonNull
        public Builder args(List<String> args) {
            this.args = args;
            return this;
        }

        @NonNull
        public Dockerfile build() {
            return new Dockerfile(baseImage, javaVersion, args);
        }
    }
}
