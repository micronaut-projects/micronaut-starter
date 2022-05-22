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
package io.micronaut.starter.feature.config;

import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;

public class ApplicationConfiguration extends Configuration {

    private static final String MAIN = "main";
    private static final String TEST = "test";

    public ApplicationConfiguration(@NonNull String sourceSet, @NonNull String environment) {
        super(sourceSet, "application-" + environment, "application-config-" + environment);
    }

    public ApplicationConfiguration(@NonNull String environment) {
        this(MAIN, environment);
    }

    public ApplicationConfiguration() {
        super(MAIN, "application", "application-config");
    }

    public static ApplicationConfiguration testConfig() {
        return new ApplicationConfiguration(TEST, Environment.TEST);
    }

    public static ApplicationConfiguration functionTestConfig() {
        return new ApplicationConfiguration(TEST, Environment.FUNCTION);
    }

    public static ApplicationConfiguration devConfig() {
        return new ApplicationConfiguration(MAIN, Environment.DEVELOPMENT);
    }
}
