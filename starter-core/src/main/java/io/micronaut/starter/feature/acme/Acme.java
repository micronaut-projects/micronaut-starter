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
package io.micronaut.starter.feature.acme;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.OneOfFeature;

import javax.inject.Singleton;

@Singleton
public class Acme implements OneOfFeature {

    @Override
    public String getName() {
        return "acme";
    }

    @Override
    public String getTitle() {
        return "ACME";
    }

    @Override
    public String getDescription() {
        return "Adds support for ACME (Automated Certificate Management Environment)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.server.ssl.enabled", "true");
        generatorContext.getConfiguration().put("acme.enabled", "true");
        generatorContext.getConfiguration().put("acme.tos-agree", "true");
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-acme/latest/guide/index.html";
    }

    @Override
    public Class<?> getFeatureClass() {
        return Acme.class;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.OTHER;
    }
}
