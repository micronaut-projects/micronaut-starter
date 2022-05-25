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
package io.micronaut.starter.feature.cache;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;

import jakarta.inject.Singleton;

@Singleton
public class EHCache implements CacheFeature {

    @Override
    public String getName() {
        return "cache-ehcache";
    }

    @Override
    public String getTitle() {
        return "EHCache Cache";
    }

    @Override
    public String getDescription() {
        return "Adds support for caching using EHCache";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.caches.my-cache.maximumSize", 20);
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.cache")
                .artifactId("micronaut-cache-ehcache")
                .compile());
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.ehcache.org/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html#ehcache";
    }

}
