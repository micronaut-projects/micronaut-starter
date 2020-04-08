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
package io.micronaut.starter.feature.lang.kotlin;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.KotlinApplicationFeature;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.KotlinTest;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.TestFramework;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Kotlin implements LanguageFeature {

    private final KotlinTest kotlinTest;
    private final List<KotlinApplicationFeature> applicationFeatures;

    public Kotlin(List<KotlinApplicationFeature> applicationFeatures, KotlinTest kotlinTest) {
        this.applicationFeatures = applicationFeatures;
        this.kotlinTest = kotlinTest;
    }

    @Override
    public String getName() {
        return "kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.hasApplicationFeature()) {
            applicationFeatures.stream()
                    .filter(f -> f.supports(featureContext.getCommand()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getProjectProperties().put("kotlinVersion", getVersion());
    }

    @Override
    public String getVersion() {
        return "1.3.50";
    }

    @Override
    public TestFeature getDefaultTestFeature() {
        return kotlinTest;
    }

    @Override
    public boolean isKotlin() {
        return true;
    }
}
