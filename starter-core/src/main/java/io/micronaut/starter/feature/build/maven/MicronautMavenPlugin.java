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
package io.micronaut.starter.feature.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.build.gradle.BuildNativeToolsGradlePlugin;
import io.micronaut.starter.feature.build.maven.templates.micronautMavenPlugin;
import io.micronaut.starter.template.RockerWritable;

import java.util.*;

public class MicronautMavenPlugin  {
    private static final String MICRONAUT_MAVEN_PLUGIN_ARTIFACT_ID = "micronaut-maven-plugin";
    private final int order;
    private final Boolean shared;
    private final String jvmArguments;
    private final List<String> appArguments;
    private final BuildNativeToolsGradlePlugin buildNativeToolsGradlePlugin;
    private final List<DependencyCoordinate> aotDependencies;
    private final List<MavenCoordinate> testResourcesDependencies;
    private String configFile;

    public MicronautMavenPlugin(
            int order,
            Boolean shared,
            String jvmArguments,
            List<String> appArguments,
            BuildNativeToolsGradlePlugin buildNativeToolsGradlePlugin,
            List<DependencyCoordinate> aotDependencies,
            List<MavenCoordinate> testResourcesDependencies,
            String configFile) {
        this.order = order;
        this.shared = shared;
        this.jvmArguments = jvmArguments;
        this.appArguments = appArguments;
        this.buildNativeToolsGradlePlugin = buildNativeToolsGradlePlugin;
        this.aotDependencies = aotDependencies;
        this.testResourcesDependencies = testResourcesDependencies;
        this.configFile = configFile;
    }

    public String getConfigFile() {
        return configFile;
    }

    public int getOrder() {
        return order;
    }

    public Boolean getShared() {
        return shared;
    }

    public String getJvmArguments() {
        return jvmArguments;
    }

    public List<String> getAppArguments() {
        return appArguments;
    }

    public BuildNativeToolsGradlePlugin getBuildNativeToolsGradlePlugin() {
        return buildNativeToolsGradlePlugin;
    }

    public List<DependencyCoordinate> getAotDependencies() {
        return aotDependencies;
    }

    public List<MavenCoordinate> getTestResourcesDependencies() {
        return testResourcesDependencies;
    }

    public MavenPlugin toMavenPlugin() {
        return new MavenPlugin(MICRONAUT_MAVEN_PLUGIN_ARTIFACT_ID,
                Collections.singletonList(new RockerWritable(micronautMavenPlugin.template(this))),
                order);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Boolean shared;
        private String jvmArguments;
        private List<String> appArguments;
        private BuildNativeToolsGradlePlugin buildNativeToolsGradlePlugin;
        private List<DependencyCoordinate> aotDependencies;
        private List<MavenCoordinate> testResourcesDependencies;
        private int order;
        private String configFile;

        private Builder() {
        }

        @NonNull
        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public Builder buildNativeToolsPlugin(BuildNativeToolsGradlePlugin buildNativeToolsGradlePlugin) {
            this.buildNativeToolsGradlePlugin = buildNativeToolsGradlePlugin;
            return this;
        }

        public Builder configFile(String configFile) {
            this.configFile = configFile;
            return this;
        }

        public Builder jvmArguments(String jvmArguments) {
            this.jvmArguments = jvmArguments;
            return this;
        }

        public Builder shared(Boolean shared) {
            this.shared = shared;
            return this;
        }

        public Builder aotDependencies(List<DependencyCoordinate> aotDependencies) {
            this.aotDependencies = aotDependencies;
            return this;
        }

        public Builder testResourcesDependencies(List<MavenCoordinate> testResourcesDependencies) {
            this.testResourcesDependencies = testResourcesDependencies;
            return this;
        }

        public MicronautMavenPlugin build() {
            return new MicronautMavenPlugin(order, shared, jvmArguments, appArguments, buildNativeToolsGradlePlugin, aotDependencies, testResourcesDependencies, configFile);
        }
    }
}
