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
package io.micronaut.starter.feature.architecture;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import jakarta.inject.Singleton;

@Singleton
public class X86 implements CpuArchitecture {
    public static final String NAME = "x86";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "x86 CPU Architecture";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "It can be used in combination with '" + Cdk.NAME + "' and " + AwsLambda.FEATURE_NAME_AWS_LAMBDA + " to generate infrastructure for the Lambda CPU architecture";
    }

    @NonNull
    public Architecture getCpuArchitecture() {
        return Architecture.X86;
    }
}
