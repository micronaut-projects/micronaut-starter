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
package io.micronaut.starter.cli.command;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.Options;

import java.util.Set;

public class GenerateOptions {
    @NonNull
    private final ApplicationType applicationType;
    @NonNull
    private final Options options;
    @NonNull
    private final Set<String> features;

    public GenerateOptions(@NonNull ApplicationType applicationType,
                           @NonNull Options options,
                           @NonNull Set<String> features) {
        this.applicationType = applicationType;
        this.options = options;
        this.features = features;
    }

    @NonNull
    public ApplicationType getApplicationType() {
        return applicationType;
    }

    @NonNull
    public Options getOptions() {
        return options;
    }

    @NonNull
    public Set<String> getFeatures() {
        return features;
    }
}
