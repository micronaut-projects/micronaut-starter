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
package io.micronaut.starter.feature.jaxrs;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.security.SecurityFeature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

@Singleton
public class JaxRs implements Feature, MicronautServerDependent {

    public static final String MICRONAUT_JAX_RS_GROUP = "io.micronaut.jaxrs";
    public static final String MICRONAUT_JAXRS_VERSION = "micronaut.jaxrs.version";
    public static final String MICRONAUT_JAXRS_PROCESSOR = "micronaut-jaxrs-processor";
    public static final String NAME = "jax-rs";

    private final JaxRsSecurity jaxRsSecurity;

    public JaxRs(JaxRsSecurity jaxRsSecurity) {
        this.jaxRsSecurity = jaxRsSecurity;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JAX-RS support";
    }

    @Override
    public String getDescription() {
        return "Adds support for using JAX-RS annotations";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(SecurityFeature.class)) {
            featureContext.addFeature(jaxRsSecurity);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder jaxrs = Dependency.builder()
                .groupId(MICRONAUT_JAX_RS_GROUP)
                .artifactId(MICRONAUT_JAXRS_PROCESSOR)
                .versionProperty(MICRONAUT_JAXRS_VERSION)
                .template();

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addDependency(jaxrs.annotationProcessor());
        } else {
            generatorContext.addDependency(MicronautDependencyUtils
                    .moduleMavenAnnotationProcessor(MICRONAUT_JAX_RS_GROUP, MICRONAUT_JAXRS_PROCESSOR, MICRONAUT_JAXRS_VERSION, false)
            );
        }

        generatorContext.addDependency(jaxrs.testAnnotationProcessor());
        generatorContext.addDependency(Dependency.builder()
                .groupId(MICRONAUT_JAX_RS_GROUP)
                .artifactId("micronaut-jaxrs-server")
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.SERVER;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html";
    }
}
