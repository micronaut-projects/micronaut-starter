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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import jakarta.inject.Singleton;

import java.util.Map;

/**
 * Coherence as a Distributed Configuration
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Requires(property = "micronaut.starter.feature.coherence.grpc.client.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class CoherenceGrpcClient implements Feature {

    public static final String NAME = "coherence-grpc-client";
    private final CoherenceFeature coherenceFeature;

    public CoherenceGrpcClient(CoherenceFeature coherenceFeature) {
        this.coherenceFeature = coherenceFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Coherence gRPC Client";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Coherence as a gRPC client";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://coherence.java.net/";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#grpc";
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

        config.put("coherence.session.default.type", "grpc");

        generatorContext.addDependency(MicronautDependencyUtils.coherenceDependency().artifactId("micronaut-coherence-grpc-client").compile());
        generatorContext.addDependency(Dependency.builder().groupId("com.oracle.coherence.ce").artifactId("coherence-java-client").compile());
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
