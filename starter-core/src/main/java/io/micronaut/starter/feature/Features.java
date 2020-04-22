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

import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.util.VersionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Features extends ArrayList<String> {

    private final List<Feature> featureList;
    private final BuildTool buildTool;
    private ApplicationFeature applicationFeature;
    private LanguageFeature languageFeature;
    private TestFeature testFeature;
    private JdkVersion javaVersion;

    public Features(List<Feature> featureList, Options options) {
        super(featureList.stream().map(Feature::getName).collect(Collectors.toList()));
        this.featureList = featureList;
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

    public List<Feature> getFeatures() {
        return featureList;
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public String getTargetJdk() {
        if (language().isJava()) {
            return VersionInfo.toJdkVersion(javaVersion.majorVersion());
        } else {
            return VersionInfo.toJdkVersion(Math.min(javaVersion.majorVersion(), 13));
        }
    }
}
