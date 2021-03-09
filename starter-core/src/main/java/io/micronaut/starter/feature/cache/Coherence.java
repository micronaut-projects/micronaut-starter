/*
 * Copyright 2021 original authors
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
package io.micronaut.starter.feature.cache;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.coherence.CoherenceFeature;

import javax.inject.Singleton;

@Singleton
public class Coherence implements CacheFeature {

    private CoherenceFeature coherenceFeature;

    public Coherence(CoherenceFeature coherenceFeature) {
        this.coherenceFeature = coherenceFeature;
    }

    @Override
    public String getName() {
        return "cache-coherence";
    }

    @Override
    public String getTitle() {
        return "Coherence Cache";
    }

    @Override
    public String getDescription() {
        return "Adds support for cache using Coherence";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://coherence.java.net/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-coherence/1.0.x/guide/#cache";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(CoherenceFeature.class)) {
            featureContext.addFeature(coherenceFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder coherenceMicronaut = Dependency.builder()
                .groupId("io.micronaut.coherence")
                .artifactId("micronaut-coherence-cache")
                .template();
        generatorContext.addDependency(coherenceMicronaut.compile());
    }

    @Override
    public boolean isPreview() {
        return true;
    }
}
