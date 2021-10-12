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
package io.micronaut.starter.feature.coherence;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

import java.util.Map;

/**
 * Coherence used to store HTTP sessions feature.
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Singleton
public class CoherenceSessionStore implements Feature {

    private final CoherenceFeature coherenceFeature;

    public CoherenceSessionStore(CoherenceFeature coherenceFeature) {
        this.coherenceFeature = coherenceFeature;
    }

    @Override
    public String getName() {
        return "coherence-session";
    }

    @Override
    public String getTitle() {
        return "Coherence HTTP Session Store";
    }

    @Override
    public String getDescription() {
        return "Adds support for storing HTTP sessions in Coherence";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://coherence.java.net/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#coherenceHttpSessions";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(CoherenceFeature.class)) {
            featureContext.addFeature(coherenceFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> config = generatorContext.getConfiguration();
        config.put("micronaut.session.http.coherence.enabled", true);

        Dependency.Builder coherenceMicronaut = Dependency.builder()
                .groupId("io.micronaut.coherence")
                .artifactId("micronaut-coherence-session")
                .template();
        generatorContext.addDependency(coherenceMicronaut.compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }
}
