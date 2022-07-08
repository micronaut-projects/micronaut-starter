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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.graalvm.GraalVM;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.starter.feature.aws.AwsV2Sdk.APACHE_CLIENT_DEPENDENCY;
import static io.micronaut.starter.feature.aws.AwsV2Sdk.URL_CONNECTION_CLIENT;
import static io.micronaut.starter.feature.aws.AwsV2Sdk.NETTY_NIO_CLIENT_DEPENDENCY;

public enum AwsSdkClient {
    APACHE(APACHE_CLIENT_DEPENDENCY),
    URL_CONNECTION(URL_CONNECTION_CLIENT),
    NETTY_IO(NETTY_NIO_CLIENT_DEPENDENCY);

    private final Dependency.Builder dependency;

    AwsSdkClient(Dependency.Builder dependency) {
        this.dependency = dependency;
    }

    @NonNull
    public Dependency.Builder getDependency() {
        return dependency;
    }

    @NonNull
    public static List<Dependency> dependencies(@NonNull GeneratorContext generatorContext,
                                                @NonNull Dependency.Builder awsSdkDependency,
                                                @NonNull AwsSdkClient clientIfGraalVM) {
        List<Dependency> result = new ArrayList<>();
        if (generatorContext.isFeaturePresent(GraalVM.class)) {
            for (AwsSdkClient v : AwsSdkClient.values()) {
                if (v != clientIfGraalVM) {
                    awsSdkDependency.exclude(v.dependency.build());
                }
            }
            result.add(clientIfGraalVM.getDependency().build());
        }
        result.add(awsSdkDependency.build());
        return result;
    }
}
