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
import jakarta.inject.Singleton;

/**
 * Adds Micronaut Camel, which runs Apache Camel routes in the application. Camel components are added
 * as plain Camel dependencies, such as {@code org.apache.camel:camel-kafka}.
 *
 * @since 5.3.0
 */
@Requires(property = "micronaut.starter.feature.camel.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Camel implements Feature {

    public static final String NAME = "camel";

    private static final Dependency CORE = MicronautDependencyUtils.camel()
            .artifactId("micronaut-camel-core")
            .compile()
            .build();
    private static final Dependency PROCESSOR = MicronautDependencyUtils.camel()
            .artifactId("micronaut-camel-processor")
            .annotationProcessor()
            .build();
    private static final Dependency TEST = MicronautDependencyUtils.camel()
            .artifactId("micronaut-camel-test")
            .test()
            .build();
    private static final Dependency DIRECT = MicronautDependencyUtils.camel()
            .artifactId("micronaut-camel-direct")
            .compile()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Apache Camel";
    }

    @Override
    public String getDescription() {
        return "Adds support for Apache Camel routes, written in the Java or YAML DSL and configured with Micronaut configuration";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.CLI;
    }

    @Override
    public String getCategory() {
        return Category.MESSAGING;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-camel/latest/guide/index.html";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://camel.apache.org/manual/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(CORE);
        generatorContext.addDependency(PROCESSOR);
        generatorContext.addDependency(TEST);
        generatorContext.addDependency(DIRECT);
        generatorContext.getConfiguration().put("camel.main.name", generatorContext.getProject().getName());
    }
}
