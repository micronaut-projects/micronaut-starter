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
package io.micronaut.starter.feature.migration;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.logging.LiquibaseSlf4j;
import io.micronaut.starter.feature.logging.Slf4jJulBridge;
import io.micronaut.starter.feature.migration.template.liquibaseChangelog;
import io.micronaut.starter.feature.migration.template.liquibaseSchema;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.liquibase.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Liquibase implements MigrationFeature, OpenRewriteFeature {

    public static final String NAME = "liquibase";

    private final Slf4jJulBridge slf4jJulBridge;

    public Liquibase(Slf4jJulBridge slf4jJulBridge) {
        this.slf4jJulBridge = slf4jJulBridge;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(LiquibaseSlf4j.class, slf4jJulBridge);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Liquibase Database Migration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Liquibase database migrations";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        generatorContext.addTemplate("liquibaseChangelog", new RockerTemplate("src/main/resources/db/liquibase-changelog.xml",
                        liquibaseChangelog.template()));
        generatorContext.addTemplate("liquibaseSchema", new RockerTemplate("src/main/resources/db/changelog/01-schema.xml",
                        liquibaseSchema.template()));
    }
}
