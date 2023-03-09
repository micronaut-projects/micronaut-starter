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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import jakarta.inject.Singleton;

@Singleton
public class AwsLambdaSnapstart implements Feature {
    @Override
    @NonNull
    public String getName() {
        return "snapstart";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "AWS Lambda SnapStart";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/lambda/latest/dg/snapstart.html";
    }

    public boolean supports(@NonNull CpuArchitecture cpuArchitecture) {
        return !(cpuArchitecture instanceof Arm);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT
                || applicationType == ApplicationType.FUNCTION;
    }
}
