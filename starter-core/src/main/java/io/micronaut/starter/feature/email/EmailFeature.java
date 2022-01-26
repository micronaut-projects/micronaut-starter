/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.view.ViewFeature;

abstract class EmailFeature implements Feature {

    static final String MICRONAUT_EMAIL_GROUP_ID = "io.micronaut.email";

    private final TemplateEmailFeature templateEmailFeature;

    EmailFeature(TemplateEmailFeature templateEmailFeature) {
        this.templateEmailFeature = templateEmailFeature;
    }

    @Override
    public String getCategory() {
        return Category.MESSAGING;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    abstract String getModule();

    @Override
    @NonNull
    public String getName() {
        return "email-" + getModule();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_EMAIL_GROUP_ID)
                .artifactId("micronaut-email-" + getModule())
                .build()
        );
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(ViewFeature.class)) {
            featureContext.addFeature(templateEmailFeature);
        }
    }
}
