/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.micrometer.MicrometerFeature;
import io.micronaut.starter.feature.testcontainers.ContributingTestContainerDependency;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public class Cassandra implements Feature, ContributingTestContainerDependency {

    public static final String NAME = "cassandra";

    private static final Dependency MICRONAUT_CASSANDRA_DEPENDENCY = MicronautDependencyUtils.cassandraDependency()
            .artifactId("micronaut-cassandra")
            .compile()
            .build();

    private static final Dependency CASSANDRA_METRICS_MICROMETER_DEPENDENCY = Dependency.builder()
            .groupId("com.datastax.oss")
            .artifactId("java-driver-metrics-micrometer")
            .compile()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Cassandra Driver";
    }

    @Override
    public String getDescription() {
        return "Adds support for Cassandra persistence";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        applyDependencies(generatorContext);
        applyConfiguration(generatorContext);
    }

    private void applyDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_CASSANDRA_DEPENDENCY);
        if (generatorContext.isFeaturePresent(MicrometerFeature.class)) {
            generatorContext.addDependency(CASSANDRA_METRICS_MICROMETER_DEPENDENCY);
        }
    }

    private void applyConfiguration(GeneratorContext generatorContext) {
        ApplicationConfiguration configuration = generatorContext.getConfiguration();
        configuration.put("cassandra.default.basic.load-balancing-policy.local-datacenter", "datacenter1");
        configuration.put("cassandra.default.basic.session-name", "defaultSession");
        configuration.put("cassandra.default.advanced.control-connection.schema-agreement.timeout", 20);
        configuration.put("cassandra.default.advanced.ssl-engine-factory", "DefaultSslEngineFactory");

        if (generatorContext.isFeaturePresent(MicrometerFeature.class)) {
            configuration.put("cassandra.default.advanced.metrics.factory.class", "MicrometerMetricsFactory");
            configuration.put("cassandra.default.advanced.metrics.session.enabled", List.of("connected-nodes", "cql-requests", "bytes-sent", "bytes-received"));
            configuration.put("cassandra.default.advanced.metrics.node.enabled", List.of("cql-requests", "bytes-sent", "bytes-received"));
        }
        if (generatorContext.isFeaturePresent(TestResources.class)) {
            configuration.put("test-resources.containers.cassandra.startup-timeout", "600s");
            configuration.put("test-resources.containers.cassandra.image-name", "cassandra");
            configuration.put("test-resources.containers.cassandra.exposed-ports", List.of(Map.of("cassandra.port", 9042)));
            configuration.put("cassandra.default.basic.contact-points", List.of("localhost:${cassandra.port}"));
        } else {
            configuration.put("cassandra.default.basic.contact-points", List.of("localhost:9042"));
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-cassandra/latest/guide/index.html";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.datastax.com/en/developer/java-driver/latest/";
    }

    @Override
    public List<Dependency> testContainersDependencies() {
        return Collections.singletonList(ContributingTestContainerDependency.testContainerDependency("cassandra"));
    }
}
