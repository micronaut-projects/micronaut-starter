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
package io.micronaut.starter.feature.awsparameterstore;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;

import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
public class AwsParameterStore implements DistributedConfigFeature {
    @Override
    public String getTitle() {
        return "AWS Parameter Store Distributed Configuration";
    }

    @Override
    public String getName() {
        return "aws-parameter-store";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with AWS System Manager Parameter Store";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.aws")
                .artifactId("micronaut-aws-parameter-store")
                .compile());

        Map<String, Object> config = populateBootstrapForDistributedConfiguration(generatorContext);
        config.put("aws.client.system-manager.parameterstore.enabled", true);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#parametersStore";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html";
    }
}
