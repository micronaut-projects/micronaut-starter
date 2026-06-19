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

@Requires(property = "micronaut.starter.feature.security.html-sanitizer.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SecurityHtmlSanitizer extends SecurityFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SECURITY_HTML_SANITIZER = "micronaut-security-html-sanitizer";
    private static final Dependency DEPENDENCY_MICRONAUT_SECURITY_HTML_SANITIZER = MicronautDependencyUtils.securityDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_SECURITY_HTML_SANITIZER)
            .compile()
            .build();

    public SecurityHtmlSanitizer(SecurityProcessor securityProcessor) {
        super(securityProcessor);
    }

    @NonNull
    @Override
    public String getName() {
        return "security-html-sanitizer";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security HTML Sanitizer";
    }

    @Override
    public @Nullable String getDescription() {
        return "HTML sanitizer backed by the OWASP Java HTML Sanitizer";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_SECURITY_HTML_SANITIZER);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#htmlSanitizer";
    }
}
