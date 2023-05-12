/*
 * Copyright 2017-2023 original authors
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

import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Singleton
public class MicronautJdkVersionConfiguration implements JdkVersionConfiguration {
    public static final List<JdkVersion> SUPPORTED_JDKS = Collections.singletonList(JdkVersion.JDK_17);

    public static final JdkVersion DEFAULT_OPTION = JdkVersion.JDK_17;

    @Override
    public List<JdkVersion> getSupportedJdkVersions() {
        return SUPPORTED_JDKS;
    }

    @Override
    public JdkVersion getDefaultJdkVersion() {
        return DEFAULT_OPTION;
    }
}
