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
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

@Singleton
public class Cassandra implements Feature {

    public static final String NAME = "cassandra";

    private static final Dependency MICRONAUT_CASSANDRA_DEPENDENCY = MicronautDependencyUtils.cassandraDependency()
            .artifactId("micronaut-cassandra")
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
        generatorContext.addDependency(MICRONAUT_CASSANDRA_DEPENDENCY);
        applyConfiguration(generatorContext);
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
            configuration.put("test-resources.containers.cassandra.exposed-ports[0].cassandra.port", 9042);
            configuration.put("cassandra.default.basic.contact-points[0]", "localhost:${cassandra.port}");
        } else {
            configuration.put("cassandra.default.basic.contact-points[0]", "localhost:9042");
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
}
