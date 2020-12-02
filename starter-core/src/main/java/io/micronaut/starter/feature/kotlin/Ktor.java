/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.kotlin;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.feature.kotlin.templates.*;
import io.micronaut.starter.feature.lang.kotlin.KotlinApplicationFeature;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class Ktor implements KotlinApplicationFeature, ServerFeature, LanguageSpecificFeature {

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() != Language.KOTLIN) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof Ktor;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("Ktor feature only supports Kotlin");
                }
            });
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "ktor";
    }

    @Override
    public String getDescription() {
        return "Support for using Ktor as the server instead of Micronaut’s native HTTP server";
    }

    @Override
    public String getTitle() {
        return "Ktor";
    }

    @Override
    public String getCategory() {
        return Category.SERVER;
    }

    @Override
    public Language getRequiredLanguage() {
        return Language.KOTLIN;
    }

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return generatorContext.getProject().getPackageName() + ".Application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        KotlinApplicationFeature.super.apply(generatorContext);

            generatorContext.addTemplate("application", new RockerTemplate("src/main/kotlin/{packagePath}/Application.kt", applicationKotlin.template(generatorContext.getProject())));

        generatorContext.addTemplate("homeRoute", new RockerTemplate("src/main/kotlin/{packagePath}/HomeRoute.kt", homeRouteKotlin.template(generatorContext.getProject())));

        generatorContext.addTemplate("jacksonFeature", new RockerTemplate("src/main/kotlin/{packagePath}/JacksonFeature.kt", jacksonFeatureKotlin.template(generatorContext.getProject())));

        generatorContext.addTemplate("nameTransformer", new RockerTemplate("src/main/kotlin/{packagePath}/NameTransformer.kt", nameTransformerKotlin.template(generatorContext.getProject())));

        generatorContext.addTemplate("uppercaseTransformer", new RockerTemplate("src/main/kotlin/{packagePath}/UppercaseTransformer.kt", uppercaseTransformerKotlin.template(generatorContext.getProject())));
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kotlin/latest/guide/index.html#ktor";
    }
}
