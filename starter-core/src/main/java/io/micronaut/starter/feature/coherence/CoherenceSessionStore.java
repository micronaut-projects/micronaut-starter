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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import jakarta.inject.Singleton;

/**
 * Coherence used to store HTTP sessions feature.
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Requires(property = "micronaut.starter.feature.coherence.session.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class CoherenceSessionStore implements Feature {

    public static final String NAME = "coherence-session";
    private final CoherenceFeature coherenceFeature;

    public CoherenceSessionStore(CoherenceFeature coherenceFeature) {
        this.coherenceFeature = coherenceFeature;
    }

    @Override
    public String getName() {
        return NAME;
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
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://coherence.java.net/";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
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
        generatorContext.getConfiguration().put("micronaut.session.http.coherence.enabled", true);

        generatorContext.addDependency(MicronautDependencyUtils.coherenceDependency().artifactId("micronaut-coherence-session").compile());
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }
}
