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

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.RequiresJavaReflection;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Requires(property = "micronaut.starter.feature.jdbc.mybatis.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
@Primary
public class MyBatis implements RequiresJavaReflection {
    public static final String NAME = "mybatis";
    public static final String MICRONAUT_MYBATIS_ARTIFACT = "micronaut-mybatis";

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MyBatis";
    }

    @Override
    public String getDescription() {
        return "It adds the Micronaut MyBatis dependency";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/#mybatis";
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://mybatis.org/mybatis-3/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.sqlDependency()
                .artifactId(MICRONAUT_MYBATIS_ARTIFACT)
                .compile());
    }
}
