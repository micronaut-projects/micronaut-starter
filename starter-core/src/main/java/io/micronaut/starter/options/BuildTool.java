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
package io.micronaut.starter.options;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.Project;

import java.util.Locale;
import java.util.Objects;

public enum BuildTool {

    GRADLE("build/libs", "build.gradle", "-*-all.jar"),
    GRADLE_KOTLIN("build/libs", "build.gradle.kts", "-*-all.jar"),
    MAVEN("target", "pom.xml", "-*.jar");

    private final String jarDirectory;
    private final String fileName;
    private final String shadeJarPattern;

    BuildTool(String jarDirectory, String fileName, String shadeJarPattern) {
        this.jarDirectory = jarDirectory;
        this.fileName = fileName;
        this.shadeJarPattern = shadeJarPattern;
    }

    public String getJarDirectory() {
        return jarDirectory;
    }

    public String getShadeJarDirectoryPattern(Project project) {
        Objects.requireNonNull(project, "Project should not be null");
        return getJarDirectory() + '/' + project.getName() + shadeJarPattern;
    }

    public String getBuildFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return getName();
    }

    @NonNull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public boolean isGradle() {
        return this == GRADLE || this == GRADLE_KOTLIN;
    }
}
