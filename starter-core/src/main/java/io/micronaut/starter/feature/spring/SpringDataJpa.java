/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.spring;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.DataJpa;

import javax.inject.Singleton;

@Singleton
public class SpringDataJpa extends SpringFeature {

    private final DataJpa dataJpa;

    public SpringDataJpa(Spring spring, DataJpa dataJpa) {
        super(spring);
        this.dataJpa = dataJpa;
    }

    @Override
    public String getName() {
        return "spring-data-jpa";
    }

    @Override
    public String getTitle() {
        return "Spring Data JPA Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring Data JPA Annotations";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(DataJpa.class)) {
            featureContext.addFeature(dataJpa);
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-spring")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-spring-jpa")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.springframework")
                .artifactId("spring-orm")
                .compile());
    }
}
