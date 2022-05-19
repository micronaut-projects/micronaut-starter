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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.aws.template.dynamodbConfigurationGroovy;
import io.micronaut.starter.feature.aws.template.dynamodbConfigurationJava;
import io.micronaut.starter.feature.aws.template.dynamodbConfigurationKotlin;
import io.micronaut.starter.feature.aws.template.dynamodbRepositoryGroovy;
import io.micronaut.starter.feature.aws.template.dynamodbRepositoryJava;
import io.micronaut.starter.feature.aws.template.dynamodbRepositoryKotlin;
import jakarta.inject.Singleton;

@Singleton
public class DynamoDb implements AwsFeature {
    public static final String ARTIFACTID_DYNAMODB = "dynamodb";
    private final AwsV2Sdk awsV2Sdk;

    public DynamoDb(AwsV2Sdk awsV2Sdk) {
        this.awsV2Sdk = awsV2Sdk;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(AwsV2Sdk.class)) {
            featureContext.addFeature(awsV2Sdk);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID_AWS_SDK_V2)
                .artifactId(ARTIFACTID_DYNAMODB)
                .compile());

        String repositoryFile = generatorContext.getSourcePath("/{packagePath}/DynamoRepository");
        generatorContext.addTemplate("dynamoRepository", repositoryFile,
                dynamodbRepositoryJava.template(generatorContext.getProject()),
                dynamodbRepositoryKotlin.template(generatorContext.getProject()),
                dynamodbRepositoryGroovy.template(generatorContext.getProject()));

        String configurationFile = generatorContext.getSourcePath("/{packagePath}/DynamoConfiguration");
        generatorContext.addTemplate("dynamoConfiguration", configurationFile,
                dynamodbConfigurationJava.template(generatorContext.getProject()),
                dynamodbConfigurationKotlin.template(generatorContext.getProject()),
                dynamodbConfigurationGroovy.template(generatorContext.getProject()));
    }

    @Override
    @NonNull
    public String getName() {
        return "dynamodb";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Amazon DynamoDB";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Integrates with Amazon DynamoDB a NoSQL database service";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/#dynamodb";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://aws.amazon.com/dynamodb/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
