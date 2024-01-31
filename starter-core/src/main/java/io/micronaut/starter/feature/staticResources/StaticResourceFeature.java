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
package io.micronaut.starter.feature.staticResources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

@Singleton
public class StaticResourceFeature implements DefaultFeature {
    @Override
    public String getName() {
        return "static-resources";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addStaticResources(generatorContext);
    }

    private void addStaticResources(@NonNull GeneratorContext generatorContext) {
        List<StaticResource> list = generatorContext.getFeatures().getFeatures()
                .stream()
                .filter(f -> f instanceof ContributingStaticResources)
                .map(f -> ((ContributingStaticResources) f).staticResources())
                .flatMap(List::stream)
                .toList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (StaticResource staticResource : list) {
                generatorContext.getConfiguration().put("micronaut.router.static-resources." + staticResource.name() + ".paths", staticResource.paths());
                generatorContext.getConfiguration().put("micronaut.router.static-resources." + staticResource.name() + ".mapping", staticResource.mapping());
            }
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT;
    }
}
