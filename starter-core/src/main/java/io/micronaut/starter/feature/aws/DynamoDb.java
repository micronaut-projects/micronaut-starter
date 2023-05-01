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
import io.micronaut.starter.feature.aws.template.ciawsconditionGroovy;
import io.micronaut.starter.feature.aws.template.ciawsconditionJava;
import io.micronaut.starter.feature.aws.template.ciawsconditionKotlin;
import io.micronaut.starter.feature.aws.template.ciawsregionconditionGroovy;
import io.micronaut.starter.feature.aws.template.ciawsregionconditionJava;
import io.micronaut.starter.feature.aws.template.ciawsregionconditionKotlin;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.config.Configuration;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.feature.validator.ValidationFeature;
import jakarta.inject.Singleton;

@Singleton
public class DynamoDb implements AwsFeature {

    public static final String ARTIFACTID_DYNAMODB = "dynamodb";
    public static final String NAME = "dynamodb";

    private final AwsV2Sdk awsV2Sdk;
    private final MicronautValidationFeature micronautValidation;

    public DynamoDb(AwsV2Sdk awsV2Sdk, MicronautValidationFeature micronautValidation) {
        this.awsV2Sdk = awsV2Sdk;
        this.micronautValidation = micronautValidation;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(AwsV2Sdk.class, awsV2Sdk);
        featureContext.addFeatureIfNotPresent(ValidationFeature.class, micronautValidation);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder dynamoDbDependency = Dependency.builder()
                .groupId(GROUP_ID_AWS_SDK_V2)
                .artifactId(ARTIFACTID_DYNAMODB)
                .compile();

        AwsSdkDependenciesUtils.dependencies(generatorContext, dynamoDbDependency)
                .forEach(generatorContext::addDependency);

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

        String ciAwsCredentialsProviderChainCondition = generatorContext.getSourcePath("/{packagePath}/CIAwsCredentialsProviderChainCondition");
        generatorContext.addTemplate("ciAwsCredentialsProviderChainCondition", ciAwsCredentialsProviderChainCondition,
                ciawsconditionJava.template(generatorContext.getProject()),
                ciawsconditionKotlin.template(generatorContext.getProject()),
                ciawsconditionGroovy.template(generatorContext.getProject()));

        String cIAwsRegionProviderChainCondition = generatorContext.getSourcePath("/{packagePath}/CIAwsRegionProviderChainCondition");
        generatorContext.addTemplate("cIAwsRegionProviderChainCondition", cIAwsRegionProviderChainCondition,
                ciawsregionconditionJava.template(generatorContext.getProject()),
                ciawsregionconditionKotlin.template(generatorContext.getProject()),
                ciawsregionconditionGroovy.template(generatorContext.getProject()));

        Configuration testConfig = generatorContext.getConfiguration("function", new ApplicationConfiguration("test", "function"));
        testConfig.put("aws.region", "us-east-1");
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
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
