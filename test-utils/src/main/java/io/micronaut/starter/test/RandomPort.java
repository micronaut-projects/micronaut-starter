/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.test;

import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class RandomPort implements DefaultFeature {

    @Override
    public String getName() {
        return "random-port";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.server.port", "-1");
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
