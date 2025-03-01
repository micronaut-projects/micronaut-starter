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
package io.micronaut.starter.feature.spring;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.database.DataJdbc;

import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.spring.data.jdbc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SpringDataJdbc extends SpringFeature {

    private final DataJdbc dataJdbc;

    public SpringDataJdbc(Spring spring, DataJdbc dataJdbc) {
        super(spring);
        this.dataJdbc = dataJdbc;
    }

    @Override
    public String getName() {
        return "spring-data-jdbc";
    }

    @Override
    public String getTitle() {
        return "Spring Data JDBC Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring Data JDBC Annotations";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(DataJdbc.class)) {
            featureContext.addFeature(dataJdbc);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.dataDependency()
                .artifactId("micronaut-data-spring")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.springframework")
                .artifactId("spring-jdbc")
                .compile());
    }
}
