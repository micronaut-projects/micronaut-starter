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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public enum TestFramework {
    JUNIT("JUnit"),
    SPOCK("Spock"),
    KOTEST("Kotest");

    public static final TestFramework DEFAULT_OPTION = JUNIT;

    private final String title;

    TestFramework(String title) {
        this.title = title;
    }

    public static boolean isKotlinTestFramework(TestFramework testFramework) {
        return testFramework == KOTEST;
    }

    public boolean isKotlinTestFramework() {
        return isKotlinTestFramework(this);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @NonNull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public String getSourcePath(String path, Language language) {
        switch (this) {
            case SPOCK:
                return Language.GROOVY.getTestSrcDir() + path + getTestFrameworkSuffix() + Language.GROOVY.getExtension();
            case KOTEST:
                return Language.KOTLIN.getTestSrcDir() + path + getTestFrameworkSuffix() + Language.KOTLIN.getExtension();
            case JUNIT:
            default:
                if (language != null) {
                    return language.getTestSrcDir() + path + getTestFrameworkSuffix()  + language.getExtension();
                } else {
                    return Language.JAVA.getTestSrcDir() + getTestFrameworkSuffix() + path + Language.JAVA.getExtension();
                }
        }
    }

    public String getTestFrameworkSuffix() {
        switch (this) {
            case SPOCK:
                return "Spec.";
            case JUNIT:
            case KOTEST:
            default:
                return "Test.";
        }
    }

    public String getTestFrameworkSuffixWithoutTrailingDot() {
        return getTestFrameworkSuffix().replace(".", "");
    }

    /**
     *
     * @return The list of supported languages for a {@link TestFramework}
     */
    public List<Language> getSupportedLanguages() {
        switch (this) {
            case SPOCK:
                return Collections.singletonList(Language.GROOVY);
            case KOTEST:
                return Collections.singletonList(Language.KOTLIN);
            case JUNIT:
                return Arrays.asList(Language.JAVA, Language.KOTLIN, Language.GROOVY);
            default:
                throw new RuntimeException("No list of supported languages have been defined for " + this.getName());
        }
    }

    /**
     *
     * @return The default language for a {@link TestFramework}
     */
    public Language getDefaultLanguage() {
        switch (this) {
            case SPOCK:
                return Language.GROOVY;
            case KOTEST:
                return Language.KOTLIN;
            case JUNIT:
                return Language.JAVA;
            default:
                throw new RuntimeException("No default language have been defined for " + this.getName());
        }
    }

    public String getTitle() {
        return title;
    }
}
