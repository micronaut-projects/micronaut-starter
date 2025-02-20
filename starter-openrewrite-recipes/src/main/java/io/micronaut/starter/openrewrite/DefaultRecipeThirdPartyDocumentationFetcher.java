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
public class DefaultRecipeThirdPartyDocumentationFetcher extends DocumentationFetcher implements RecipeThirdPartyDocumentationFetcher {
    private static final Function<String, Boolean> LINK_NOT_MICRONAUT_DOC =
            s -> s.startsWith("http") && !DefaultRecipeMicronautDocumentationFetcher.MICRONAUT_DOCUMENTATION_LINK.apply(s);

    public DefaultRecipeThirdPartyDocumentationFetcher(Environment env) {
        super(env, LINK_NOT_MICRONAUT_DOC);
    }

    @Override
    public Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName) {
        return findLinkByRecipeName(recipeName);
    }
}
