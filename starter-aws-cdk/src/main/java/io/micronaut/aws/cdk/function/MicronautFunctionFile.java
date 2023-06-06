/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.aws.cdk.function;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.options.BuildTool;

/**
 * Utility class to generate the filename to upload to AWS Lambda for both Gradle and Maven build tools.
 * @author Sergio del Amo
 * @since 3.4.0
 */
public final class MicronautFunctionFile {
    private static final String OPTIMIZED = "optimized";
    private static final String JAR = "jar";
    private static final String ALL = "all";
    private static final String DASH = "-";
    private static final String LAMBDA = "lambda";
    private static final String ZIP = "zip";
    private static final String DOT = ".";
    private static final String FUNCTION = "function";

    private MicronautFunctionFile() {

    }

    /**
     *
     * @return The Builder
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Micronaut Function File Builder.
     */
    public static class Builder {
        private BuildTool buildTool = BuildTool.GRADLE;

        private String version;

        private boolean optimized;

        private boolean graalVMNative;

        private String archiveBaseName;

        /**
         *
         * @param buildTool Build Tool
         * @return Builder
         */
        @NonNull
        public Builder buildTool(@NonNull BuildTool buildTool) {
            this.buildTool = buildTool;
            return this;
        }

        /**
         *
         * @param version Version
         * @return Builder
         */
        @NonNull
        public Builder version(@NonNull String version) {
            this.version = version;
            return this;
        }

        /**
         *
         * @param archiveBaseName Archive base name
         * @return Builder
         */
        @NonNull
        public Builder archiveBaseName(@NonNull String archiveBaseName) {
            this.archiveBaseName = archiveBaseName;
            return this;
        }

        /**
         *
         * @param optimized AOT Optimized
         * @return Builder
         */
        @NonNull
        public Builder optimized(boolean optimized) {
            this.optimized = optimized;
            return this;
        }

        /**
         * AOT Optimized.
         * @return Builder
         */
        @NonNull
        public Builder optimized() {
            return optimized(true);
        }

        /**
         *
         * @param graalVMNative GraalVM Native Image
         * @return Builder
         */
        @NonNull
        public Builder graalVMNative(boolean graalVMNative) {
            this.graalVMNative = graalVMNative;
            return this;
        }

        /**
         * GraalVM Native Image.
         * @return Builder
         */
        @NonNull
        public Builder graalVMNative() {
            return graalVMNative(true);
        }

        /**
         *
         * @return Filename
         */
        @NonNull
        public String build() {
            switch (buildTool) {
                case MAVEN:
                    if (graalVMNative) {
                        return String.join(DOT, FUNCTION, ZIP);
                    } else {
                        return String.join(DOT, version != null ?
                                new String[] { String.join(DASH, archiveBaseName, version), JAR } :
                                new String[] { archiveBaseName, JAR });
                    }
                case GRADLE_KOTLIN:
                case GRADLE:
                default:
                    return String.join(DOT, String.join(DASH, graalVMNative ?
                                    gradleZipFilenameComponents(optimized, archiveBaseName, version) :
                                    gradleJarFilenameComponents(optimized, archiveBaseName, version)),
                            graalVMNative ? ZIP : JAR);
            }
        }

        private static String[] gradleJarFilenameComponents(boolean optimized, String archiveBaseName, String version) {
            if (version != null) {
                return optimized ?
                        new String[] { archiveBaseName, version, ALL, OPTIMIZED } :
                        new String[] { archiveBaseName, version, ALL };
            } else {
                return optimized ?
                        new String[] { archiveBaseName, ALL, OPTIMIZED } :
                        new String[] { archiveBaseName, ALL };
            }
        }

        private static String[] gradleZipFilenameComponents(boolean optimized,
                                                            @NonNull String archiveBaseName,
                                                            @Nullable String version) {
            if (version != null) {
                return optimized ?
                        new String[] { archiveBaseName, version, OPTIMIZED, LAMBDA } :
                        new String[] { archiveBaseName, version, LAMBDA };
            } else {
                return optimized ?
                        new String[] { archiveBaseName, OPTIMIZED, LAMBDA } :
                        new String[] { archiveBaseName, LAMBDA  };
            }
        }
    }
}
