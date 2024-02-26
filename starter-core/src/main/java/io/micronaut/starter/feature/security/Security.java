/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.ContributingInterceptUrlMapFeature;
import io.micronaut.starter.feature.InterceptUrlMap;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Singleton
public class Security extends SecurityFeature {

    public static final String NAME = "security";

    public Security(SecurityProcessor securityProcessor) {
        super(securityProcessor);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Security";
    }

    @Override
    public String getDescription() {
        return "Adds a full featured and customizable security solution";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.security")
                .artifactId("micronaut-security")
                .compile());
        addInterceptUrlMapConfiguration(generatorContext);
    }

    protected void addInterceptUrlMapConfiguration(@NonNull GeneratorContext generatorContext) {
        List<Map<String, String>> list = generatorContext.getFeatures().getFeatures()
                .stream()
                .filter(f -> f instanceof ContributingInterceptUrlMapFeature)
                .map(f -> ((ContributingInterceptUrlMapFeature) f).interceptUrlMaps())
                .flatMap(List::stream)
                .map(InterceptUrlMap::toMap)
                .toList();
        if (CollectionUtils.isNotEmpty(list)) {
            generatorContext.getConfiguration().put("micronaut.security.intercept-url-map", list);
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html";
    }
}
