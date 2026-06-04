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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;

import jakarta.inject.Singleton;
import java.util.Map;

@Requires(property = "micronaut.starter.feature.aws.parameter.store.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AwsParameterStore implements DistributedConfigFeature {
    private static final String ARTIFACT_ID_MICRONAUT_AWS_PARAMETER_STORE = "micronaut-aws-parameter-store";
    private static final Dependency.Builder DEPENDNCY_MICRONAUT_AWS_PARAMETER_STORE = MicronautDependencyUtils.awsDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_AWS_PARAMETER_STORE)
            .compile();
    private static final String PROPERTY_MICRONAUT_CONFIG_IMPORT = "micronaut.config.import";

    public static final Map<String, Object> PROPERTIES_AWS_DISTRIBUTED_CONFIGURATION = Map.of(
            PROPERTY_MICRONAUT_CONFIG_IMPORT, "optional:parameterstore:///config/" + "${micronaut.application.name}");

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
        addDependencies(generatorContext);
        addConfigurationProperties(generatorContext);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#parametersStore";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html";
    }

    protected void addConfigurationProperties(@NonNull GeneratorContext generatorContext) {
        populateConfigurationForDistributedConfiguration(generatorContext).putAll(PROPERTIES_AWS_DISTRIBUTED_CONFIGURATION);
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDNCY_MICRONAUT_AWS_PARAMETER_STORE);
    }
}
