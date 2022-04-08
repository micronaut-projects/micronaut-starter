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
package io.micronaut.starter.analytics;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.function.CloudProvider;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.VersionInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Introspected
public class Generated {
    private final ApplicationType type;
    private final Language language;
    private final BuildTool buildTool;
    private final TestFramework testFramework;
    private final JdkVersion jdkVersion;
    private final CloudProvider cloudProvider;
    private Collection<? extends SelectedFeature> features = new ArrayList<>();
    private final String micronautVersion;

    @Creator
    public Generated(
            @NonNull ApplicationType type,
            @NonNull Language language,
            @NonNull BuildTool buildTool,
            @NonNull TestFramework testFramework,
            @NonNull JdkVersion jdkVersion,
            @Nullable CloudProvider cloudProvider) {
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.buildTool = Objects.requireNonNull(buildTool, "Build tool cannot be null");
        this.testFramework = Objects.requireNonNull(testFramework, "Test framework cannot be null");
        this.jdkVersion = Objects.requireNonNull(jdkVersion, "JDK version cannot be null");
        this.cloudProvider = cloudProvider;
        this.micronautVersion = VersionInfo.getMicronautVersion();
    }

    /**
     * @return The selected features.
     */
    public Collection<? extends SelectedFeature> getSelectedFeatures() {
        return features;
    }

    public void setSelectedFeatures(Collection<? extends SelectedFeature> features) {
        if (features != null) {
            this.features = features;
        }
    }

    /**
     * @return The type
     */
    public @NonNull ApplicationType getType() {
        return type;
    }

    /**
     * @return The language
     */
    public @NonNull Language getLanguage() {
        return language;
    }

    /**
     * @return The build tool
     */
    public @NonNull BuildTool getBuildTool() {
        return buildTool;
    }

    /**
     * @return The test framework
     */
    public @NonNull TestFramework getTestFramework() {
        return testFramework;
    }

    /**
     * @return The JDK version
     */
    public @NonNull JdkVersion getJdkVersion() {
        return jdkVersion;
    }

    /**
     * @return The selected cloud provider
     */
    public @Nullable
    CloudProvider getCloud() {
        return cloudProvider;
    }

    /**
     * @return The Micronaut Version
     */
    public @NonNull String getMicronautVersion() {
        return micronautVersion;
    }

}
