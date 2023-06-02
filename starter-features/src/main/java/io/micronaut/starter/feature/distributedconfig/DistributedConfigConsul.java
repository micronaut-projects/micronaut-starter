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
package io.micronaut.starter.feature.distributedconfig;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.consul.Consul;
import jakarta.inject.Singleton;

@Singleton
public class DistributedConfigConsul implements DistributedConfigFeature {

    private final Consul consul;

    public DistributedConfigConsul(Consul consul) {
        this.consul = consul;
    }

    @NonNull
    @Override
    public String getName() {
        return "config-consul";
    }

    @Override
    public String getTitle() {
        return "Consul Distributed Configuration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Consul";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Consul.class)) {
            featureContext.addFeature(consul);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.discovery")
                .artifactId("micronaut-discovery-client")
                .compile());
        populateBootstrapForDistributedConfiguration(generatorContext);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.consul.io";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#distributedConfigurationConsul";
    }
}
