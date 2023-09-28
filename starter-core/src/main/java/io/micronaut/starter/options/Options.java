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
package io.micronaut.starter.options;

import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.convert.value.ConvertibleValues;
import io.micronaut.core.convert.value.ConvertibleValuesMap;
import io.micronaut.starter.util.VersionInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Options implements ConvertibleValues<Object> {

    public static final String FRAMEWORK_MICRONAUT = "Micronaut";
    private final String framework;
    private final Language language;
    private final TestFramework testFramework;
    private final BuildTool buildTool;
    private final JdkVersion javaVersion;
    private final ConvertibleValuesMap<Object> additionalOptions;

    public Options(Language language, TestFramework testFramework, BuildTool buildTool, JdkVersion javaVersion) {
        this(language, testFramework, buildTool, javaVersion, Collections.emptyMap());
    }

    public Options(Language language, TestFramework testFramework, BuildTool buildTool) {
        this(language, testFramework, buildTool, VersionInfo.getJavaVersion(), Collections.emptyMap());
    }

    public Options(Language language, TestFramework testFramework) {
        this(language, testFramework, language.getDefaults().getBuild(), VersionInfo.getJavaVersion(), Collections.emptyMap());
    }

    public Options(Language language, BuildTool buildTool) {
        this(language, language.getDefaults().getTest(), buildTool, VersionInfo.getJavaVersion(), Collections.emptyMap());
    }

    public Options(Language language) {
        this(language, language.getDefaults().getTest(), language.getDefaults().getBuild(), VersionInfo.getJavaVersion(), Collections.emptyMap());
    }

    public Options() {
        this(Language.DEFAULT_OPTION, TestFramework.JUNIT, BuildTool.DEFAULT_OPTION, VersionInfo.getJavaVersion(), Collections.emptyMap());
    }

    public Options(Language language, TestFramework testFramework, BuildTool buildTool, Map<String, Object> additionalOptions) {
        this(language, testFramework, buildTool, VersionInfo.getJavaVersion(), additionalOptions);
    }

    public Options(Language language, TestFramework testFramework, BuildTool buildTool, JdkVersion javaVersion, Map<String, Object> additionalOptions) {
        this(language, testFramework, buildTool, javaVersion, additionalOptions, FRAMEWORK_MICRONAUT);
    }

    public Options(Language language, TestFramework testFramework, BuildTool buildTool, JdkVersion javaVersion, Map<String, Object> additionalOptions, String framework) {
        this.javaVersion = javaVersion;
        this.language = language;
        this.testFramework = testFramework;
        this.buildTool = buildTool;
        this.additionalOptions = new ConvertibleValuesMap<>(additionalOptions);
        this.framework = framework == null ? FRAMEWORK_MICRONAUT : framework;
    }

    public String getFramework() {
        return framework;
    }

    public Language getLanguage() {
        return language;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }

    @Override
    public Set<String> names() {
        return additionalOptions.names();
    }

    @Override
    public Collection<Object> values() {
        return additionalOptions.values();
    }

    @Override
    public <T> Optional<T> get(CharSequence name, ArgumentConversionContext<T> conversionContext) {
        return additionalOptions.get(name, conversionContext);
    }

    public JdkVersion getJavaVersion() {
        return javaVersion;
    }

    public Map<String, Object> getAdditionalOptions() {
        return additionalOptions.asMap();
    }

    public Options withLanguage(Language language) {
        return new Options(language, testFramework, buildTool, javaVersion, additionalOptions.asMap(), framework == null ? FRAMEWORK_MICRONAUT : framework);
    }

    public Options withTestFramework(TestFramework testFramework) {
        return new Options(language, testFramework, buildTool, javaVersion, additionalOptions.asMap(), framework == null ? FRAMEWORK_MICRONAUT : framework);
    }

    public Options withBuildTool(BuildTool buildTool) {
        return new Options(language, testFramework, buildTool, javaVersion, additionalOptions.asMap(), framework == null ? FRAMEWORK_MICRONAUT : framework);
    }

    public Options withJavaVersion(JdkVersion javaVersion) {
        return new Options(language, testFramework, buildTool, javaVersion, additionalOptions.asMap(), framework == null ? FRAMEWORK_MICRONAUT : framework);
    }

    public Options withFramework(String framework) {
        return new Options(language, testFramework, buildTool, javaVersion, additionalOptions.asMap(), framework);
    }
}
