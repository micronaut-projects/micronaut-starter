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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class AwsV2Sdk implements AwsFeature {

    public static final String ARTIFACT_ID_MICRONAUT_AWS_SDK_V_2 = "micronaut-aws-sdk-v2";
    static final Dependency.Builder URL_CONNECTION_CLIENT = Dependency.builder()
            .groupId(GROUP_ID_AWS_SDK_V2)
            .artifactId("url-connection-client")
            .compile();
    static final Dependency APACHE_CLIENT_DEPENDENCY = Dependency.builder()
            .groupId(GROUP_ID_AWS_SDK_V2)
            .artifactId("apache-client")
            .compile()
    static final Dependency NETTY_NIO_CLIENT_DEPENDENCY = Dependency.builder()
            .groupId(GROUP_ID_AWS_SDK_V2)
            .artifactId("netty-nio-client")
            .compile()

    @Override
    @NonNull
    public String getName() {
        return "aws-v2-sdk";
    }

    @Override
    public String getTitle() {
        return "AWS SDK 2.x";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Provides integration with the AWS SDK 2.x";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/welcome.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID_MICRONAUT_AWS)
                .artifactId(ARTIFACT_ID_MICRONAUT_AWS_SDK_V_2)
                .compile());
    }
}
