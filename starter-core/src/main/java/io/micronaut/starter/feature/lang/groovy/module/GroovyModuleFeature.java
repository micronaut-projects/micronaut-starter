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
package io.micronaut.starter.feature.lang.groovy.module;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

/**
 * Implementation note: GroovyModuleFeature is not a LanguageSpecificFeature,
 * because it's perfectly reasonable to use for Spock framework, and the
 * GroovyModuleFeature dependencies are added as test scope by default.
 *
 * @see GroovyModuleFeatureValidator
 */
public interface GroovyModuleFeature extends Feature {

    @Override
    default void apply(GeneratorContext generatorContext) {
        Dependency.Builder builder = Dependency.builder()
                .groupId("org.apache.groovy")
                .artifactId(getName());
        if (generatorContext.getLanguage() == Language.GROOVY) {
            // if language is Groovy, add to compile classpath
            generatorContext.addDependency(builder.compile());
        } else if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            // if language is not Groovy, but test if Spock, add to test classpath
            generatorContext.addDependency(builder.test());
        }
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    @NonNull
    default String getThirdPartyDocumentation() {
        return "https://docs.groovy-lang.org/docs/latest/html/documentation/";
    }

    @Override
    @NonNull
    default String getCategory() {
        return Category.GROOVY_MODULE;
    }
}
