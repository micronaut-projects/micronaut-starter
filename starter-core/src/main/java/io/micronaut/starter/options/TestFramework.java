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
import java.util.Locale;

public enum TestFramework {
    JUNIT,
    SPOCK,
    KOTEST,
    KOTLINTEST;

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
                return Language.GROOVY.getTestSrcDir() + path + "Spec." + Language.GROOVY.getExtension();
            case KOTEST:
            case KOTLINTEST:
                return Language.KOTLIN.getTestSrcDir() + path + "Test." + Language.KOTLIN.getExtension();
            case JUNIT:
            default:
                if (language != null) {
                    return language.getTestSrcDir() + path + "Test."  + language.getExtension();
                } else {
                    return Language.JAVA.getTestSrcDir() + "Test." + path + Language.JAVA.getExtension();
                }
        }
    }
}
