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
package io.micronaut.starter.feature.messaging.jms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.Oracle;
import jakarta.inject.Singleton;

@Singleton
public class OracleAdvancedQueuing extends AbstractJmsFeature {

    public static final String NAME = "jms-oracle-aq";

    private final JmsCore jmsCore;
    private final Oracle oracle;

    public OracleAdvancedQueuing(JmsCore jmsCore, Oracle oracle) {
        this.jmsCore = jmsCore;
        this.oracle = oracle;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Oracle Advanced Queuing";
    }

    @Override
    public String getDescription() {
        return "Adds support for Oracle Advanced Queuing JMS messaging";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(JmsCore.class)) {
            featureContext.addFeature(jmsCore);
        }

        if (!featureContext.isPresent(Oracle.class)) {
            featureContext.addFeature(oracle);
        }
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.oracle.com/en/database/oracle/oracle-database/21/adque/aq-introduction.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("javax.transaction")
                .artifactId("jta")
                .version("1.1")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId("com.oracle.database.messaging")
                .artifactId("aqapi")
                .version("19.3.0.0")
                .compile());
    }
}
