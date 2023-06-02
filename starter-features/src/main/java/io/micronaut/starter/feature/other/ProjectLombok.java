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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

@Singleton
public class ProjectLombok implements LanguageSpecificFeature {

    @Override
    public String getName() {
        return "lombok";
    }

    @Override
    public String getTitle() {
        return "Project Lombok";
    }

    @Override
    public String getDescription() {
        return "Adds support for Project Lombok";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#lombok";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://projectlombok.org/features/all";
    }

    @Override
    public Language getRequiredLanguage() {
        return Language.JAVA;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder lombok = Dependency.builder()
                .groupId("org.projectlombok")
                .artifactId("lombok")
                .template();

        generatorContext.addDependency(
                lombok.versionProperty("lombok.version").order(Priority.LOMBOK.getOrder()).annotationProcessor(true)
        );
        generatorContext.addDependency(lombok.compileOnly());
    }
}
