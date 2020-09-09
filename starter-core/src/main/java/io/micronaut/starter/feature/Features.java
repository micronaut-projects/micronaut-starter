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
package io.micronaut.starter.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.util.VersionInfo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Features extends ArrayList<String> {

    private final Set<Feature> featureList;
    private final BuildTool buildTool;
    private final GeneratorContext context;
    private ApplicationFeature applicationFeature;
    private LanguageFeature languageFeature;
    private TestFeature testFeature;
    private final JdkVersion javaVersion;

    public Features(GeneratorContext context, Set<Feature> featureList, Options options) {
        super(featureList.stream().map(Feature::getName).collect(Collectors.toList()));
        this.featureList = featureList;
        this.context = context;
        for (Feature feature: featureList) {
            if (applicationFeature == null && feature instanceof ApplicationFeature) {
                applicationFeature = (ApplicationFeature) feature;
            }
            if (languageFeature == null && feature instanceof LanguageFeature) {
                languageFeature = (LanguageFeature) feature;
            }
            if (testFeature == null && feature instanceof TestFeature) {
                testFeature = (TestFeature) feature;
            }
        }
        this.javaVersion = options.getJavaVersion();
        this.buildTool = options.getBuildTool();
    }

    public boolean hasFunctionFeature() {
        return getFeatures().stream().anyMatch(f -> f instanceof FunctionFeature);
    }

    public BuildTool build() {
        return buildTool;
    }

    public ApplicationFeature application() {
        return applicationFeature;
    }

    public LanguageFeature language() {
        return languageFeature;
    }

    public TestFeature testFramework() {
        return testFeature;
    }

    public Set<Feature> getFeatures() {
        return featureList;
    }

    public JdkVersion javaVersion() {
        return javaVersion;
    }

    /**
     * @return The main class
     */
    public Optional<String> mainClass() {
        ApplicationFeature application = application();
        if (application != null && context != null) {
            return Optional.ofNullable(application.mainClassName(context));
        }
        return Optional.empty();
    }

    public String getTargetJdk() {
        if (language().isJava() && testFramework().isJunit()) {
            return VersionInfo.toJdkVersion(javaVersion.majorVersion());
        } else {
            return VersionInfo.toJdkVersion(Math.min(javaVersion.majorVersion(), 13));
        }
    }

    public boolean isFeaturePresent(Class<? extends Feature> feature) {
        Objects.requireNonNull(feature, "The feature class cannot be null");
        return getFeatures().stream()
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }

    public <T extends Feature> Optional<T> getFeature(Class<T> feature) {
        Objects.requireNonNull(feature, "The feature class cannot be null");
        for (Feature f : featureList) {
            if (feature.isInstance(f)) {
                return Optional.of((T) f);
            }
        }
        return Optional.empty();
    }

    public <T extends Feature> T getRequiredFeature(Class<T> feature) {
        Objects.requireNonNull(feature, "The feature class cannot be null");
        for (Feature f : featureList) {
            if (feature.isInstance(f)) {
                return (T) f;
            }
        }
        throw new IllegalStateException(String.format("The required feature type %s does not exist", feature.getName()));
    }
}
