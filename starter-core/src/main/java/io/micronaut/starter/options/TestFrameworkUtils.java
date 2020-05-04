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

import java.util.Arrays;
import java.util.List;

public final class TestFrameworkUtils {

    private TestFrameworkUtils() {

    }

    public static List<TestFramework> supportedTestFrameworksByLanguage(Language language) {
        if (language == Language.JAVA | language == Language.GROOVY) {
            return Arrays.asList(TestFramework.JUNIT, TestFramework.SPOCK);
        }
        if (language == Language.KOTLIN) {
            return Arrays.asList(TestFramework.KOTLINTEST, TestFramework.JUNIT);
        }
        return Arrays.asList(TestFramework.values());
    }
}
