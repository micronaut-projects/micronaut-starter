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
package io.micronaut.starter.feature.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Requires(property = "micronaut.starter.feature.security.ojdbc-extensions.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SecurityOjdbcExtensions extends SecurityFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SECURITY_OJDBC_EXTENSIONS = "micronaut-security-ojdbc-extensions";
    private static final Dependency DEPENDENCY_MICRONAUT_SECURITY_OJDBC_EXTENSIONS = MicronautDependencyUtils.securityDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_SECURITY_OJDBC_EXTENSIONS)
            .compile()
            .build();

    public SecurityOjdbcExtensions(SecurityProcessor securityProcessor) {
        super(securityProcessor);
    }

    @NonNull
    @Override
    public String getName() {
        return "security-ojdbc-extensions";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security Oracle JDBC Extensions";
    }

    @Override
    public @Nullable String getDescription() {
        return "Module that registers an Oracle JDBC EndUserSecurityContextProvider";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_SECURITY_OJDBC_EXTENSIONS);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#ojdbcExtensions";
    }
}
