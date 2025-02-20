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
package io.micronaut.starter.openrewrite;

import jakarta.inject.Singleton;
import org.openrewrite.config.Environment;

import java.util.Optional;
import java.util.function.Function;

@Singleton
public class DefaultRecipeMicronautDocumentationFetcher extends DocumentationFetcher implements RecipeMicronautDocumentationFetcher {
    public static final Function<String, Boolean> MICRONAUT_DOCUMENTATION_LINK =
            s -> (s.startsWith("https://micronaut-projects.github.io") || s.startsWith("https://docs.micronaut.io"));

    public DefaultRecipeMicronautDocumentationFetcher(Environment env) {
        super(env, MICRONAUT_DOCUMENTATION_LINK);
    }

    @Override
    public Optional<String> findMicronautDocumentationByRecipeName(String recipeName) {
        return findLinkByRecipeName(recipeName);
    }
}
