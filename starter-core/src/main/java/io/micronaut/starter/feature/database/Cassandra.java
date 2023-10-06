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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.micrometer.MicrometerFeature;
import jakarta.inject.Singleton;

@Singleton
public class Cassandra implements Feature {
    public static final String NAME = "cassandra";

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
        generatorContext.getConfiguration().put("cassandra.default.clusterName", "\"myCluster\"");
        generatorContext.getConfiguration().put("cassandra.default.contactPoint", "\"localhost\"");
        generatorContext.getConfiguration().put("cassandra.default.port", 9042);
        generatorContext.getConfiguration().put("cassandra.default.maxSchemaAgreementWaitSeconds", 20);
        generatorContext.getConfiguration().put("cassandra.default.ssl", true);
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.cassandra")
                .artifactId("micronaut-cassandra")
                .compile());

        if (generatorContext.isFeaturePresent(MicrometerFeature.class)) {
            generatorContext.getConfiguration().put("cassandra.default.advanced.metrics.factory.class", "MicrometerMetricsFactory");
            generatorContext.getConfiguration().put("cassandra.default.advanced.metrics.factory.class", "MicrometerMetricsFactory");
            generatorContext.getConfiguration().put("cassandra.default.advanced.metrics.session.enabled", "[connected-nodes, cql-requests, bytes-sent, bytes-received]");
            generatorContext.getConfiguration().put("cassandra.default.advanced.metrics.node.enabled", "[cql-requests, bytes-sent, bytes-received]");
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
}
