/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.camel;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

/**
 * Adds Camel's {@code platform-http} component and REST DSL, served by the Micronaut HTTP server.
 *
 * @since 5.3.0
 */
@Requires(property = "micronaut.starter.feature.camel.http.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class CamelHttp implements Feature {

    public static final String NAME = "camel-http";

    private static final Dependency HTTP = MicronautDependencyUtils.camel()
            .artifactId("micronaut-camel-http")
            .compile()
            .build();
    private static final Dependency REST = Dependency.builder()
            .groupId(Camel.GROUP_ID_CAMEL)
            .artifactId("camel-rest")
            .compile()
            .build();

    private final Camel camel;

    public CamelHttp(Camel camel) {
        this.camel = camel;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Apache Camel HTTP and REST";
    }

    @Override
    public String getDescription() {
        return "Serves Camel platform-http routes and the REST DSL with the Micronaut HTTP server";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.MESSAGING;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-camel/latest/guide/index.html#http";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Camel.class)) {
            featureContext.addFeature(camel);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(HTTP);
        generatorContext.addDependency(REST);
    }
}
