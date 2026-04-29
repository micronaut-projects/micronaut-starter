/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.langchain4j.embeddedstore;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.Hikari;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.langchain4j.Langchain4jEmbeddedStore;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.langchain4j.store.oracle.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OracleLangchain4jEmbeddedStore implements Langchain4jEmbeddedStore {
    private static final String NAME = "langchain4j-store-oracle";
    private static final String ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_STORE_ORACLE = "micronaut-langchain4j-store-oracle";
    private static final Dependency DEPENDENCY_MICRONAUT_LANGCHAIN4J_STORE_ORACLE = MicronautDependencyUtils.langchain4j()
            .artifactId(ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_STORE_ORACLE)
            .compile()
            .build();

    private final Hikari hikari;

    public OracleLangchain4jEmbeddedStore(Hikari hikari) {
        this.hikari = hikari;
    }

    @Override
    public String getTitle() {
        return "Elastic Search" + Langchain4jEmbeddedStore.super.getTitle();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addDependencies(GeneratorContext generatorContext) {
        Langchain4jEmbeddedStore.super.addDependencies(generatorContext);
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_LANGCHAIN4J_STORE_ORACLE);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(hikari);
        }
    }
}
