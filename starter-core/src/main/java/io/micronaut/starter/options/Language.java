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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.defaults.IncludesDefaults;
import io.micronaut.starter.defaults.LanguageDefaults;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.LanguageSpecificFeature;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public enum Language implements IncludesDefaults<LanguageDefaults> {
    JAVA("java", new LanguageDefaults(TestFramework.JUNIT, BuildTool.GRADLE_KOTLIN)),
    GROOVY("groovy", new LanguageDefaults(TestFramework.SPOCK, BuildTool.GRADLE_KOTLIN)),
    KOTLIN("kt", new LanguageDefaults(TestFramework.JUNIT, BuildTool.GRADLE_KOTLIN));

    public static final Language DEFAULT_OPTION = JAVA;

    private final String extension;
    private final LanguageDefaults defaults;

    Language(String extension, LanguageDefaults defaults) {
        this.extension = extension;
        this.defaults = defaults;
    }

    /**
     * @return The extensions
     */
    public String getExtension() {
        return extension;
    }

    public static String[] extensions() {
        return Arrays.stream(values()).map(Language::getExtension).toArray(String[]::new);
    }

    public static String[] srcDirs() {
        return Arrays.stream(values()).map(Language::getSrcDir).toArray(String[]::new);
    }

    public static String[] testSrcDirs() {
        return Arrays.stream(values()).map(Language::getTestSrcDir).toArray(String[]::new);
    }

    public String getSrcDir() {
        return "src/main/" + getName();
    }

    public String getTestSrcDir() {
        return "src/test/" + getName();
    }

    public String getSourcePath(String path) {
        return getSrcDir() + path + "." + getExtension();
    }

    public String getTestSourcePath(String path) {
        return getTestSrcDir() + path + "." + getExtension();
    }

    public static Language infer(Set<Feature> features) {
        return features.stream()
                .filter(LanguageSpecificFeature.class::isInstance)
                .map(LanguageSpecificFeature.class::cast)
                .map(LanguageSpecificFeature::getRequiredLanguage)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return getName();
    }

    @NonNull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public LanguageDefaults getDefaults() {
        return defaults;
    }
}
