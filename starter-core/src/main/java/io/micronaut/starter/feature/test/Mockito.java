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
package io.micronaut.starter.feature.test;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Mockito implements Feature {

    @Override
    public String getName() {
        return "mockito";
    }

    @Override
    public String getTitle() {
        return "Mockito Framework";
    }

    @Override
    public String getDescription() {
        return "Mockito test mocking framework for JUnit";
    }

//    @Override
//    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
//        TestFramework selectedTest = options.getTestFramework();
//        if (selectedTest == null) {
//            selectedTest = options.getLanguage().getDefaults().getTest();
//        }
//        return supports(applicationType) && selectedTest == TestFramework.JUNIT;
//    }

//    @Override
//    public void apply(GeneratorContext generatorContext) {
//
//    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://site.mockito.org";
    }
}
