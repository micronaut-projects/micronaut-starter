/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.coherence;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;
import jakarta.inject.Singleton;

import java.util.Map;

/**
 * Coherence used as Distributed Configuration feature.
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Singleton
public class CoherenceDistributedConfiguration implements DistributedConfigFeature {

    public static final String NAME = "coherence-distributed-configuration";
    private final CoherenceFeature coherenceFeature;

    public CoherenceDistributedConfiguration(CoherenceFeature coherenceFeature) {
        this.coherenceFeature = coherenceFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Coherence Distributed Configuration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration using Coherence";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://coherence.java.net/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#distributedConfiguration";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(CoherenceFeature.class)) {
            featureContext.addFeature(coherenceFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> config;
        if (generatorContext.isFeaturePresent(DistributedConfigFeature.class)) {
            config = generatorContext.getBootstrapConfiguration();
        } else {
            config = generatorContext.getConfiguration();
        }

        config.put("coherence.client.enabled", true);
        config.put("coherence.client.host", "${COHERENCE_HOST:localhost}");
        config.put("coherence.client.port", "${COHERENCE_PORT:1408}");

        Dependency.Builder distributedConfiguration = MicronautDependencyUtils.coherenceDependency().artifactId("micronaut-coherence-distributed-configuration").compile();
        generatorContext.addDependency(distributedConfiguration);

        if (generatorContext.getBuildTool().isGradle() && !generatorContext.isFeaturePresent(CoherenceGrpcClient.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("com.oracle.coherence.ce")
                    .artifactId("coherence-java-client")
                    .compile());
        }
    }
}
