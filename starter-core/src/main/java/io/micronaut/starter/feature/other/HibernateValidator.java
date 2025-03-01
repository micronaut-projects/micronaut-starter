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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.validator.ValidationFeature;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.hibernate.validator.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class HibernateValidator implements ValidationFeature {

    @Override
    public String getName() {
        return "hibernate-validator";
    }

    @Override
    public String getTitle() {
        return "Hibernate Validator";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Hibernate Validator";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.beanvalidation")
                .artifactId("micronaut-hibernate-validator")
                .compile());
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-hibernate-validator/latest/guide/index.html";
    }
}
