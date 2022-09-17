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
package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.Spock;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Singleton
public class Groovy implements LanguageFeature {
    protected static final Dependency DEPENDENCY_MICRONAUT_GROOVY_RUNTIME = MicronautDependencyUtils.groovyDependency()
            .artifactId("micronaut-runtime-groovy")
            .compile()
            .build();

    protected final List<GroovyApplicationFeature> applicationFeatures;

    public Groovy(List<GroovyApplicationFeature> applicationFeatures, Spock spock) {
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        processSelectedFeatured(featureContext, feature -> true);
    }

    protected void processSelectedFeatured(FeatureContext featureContext, Predicate<Feature> filter) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            applicationFeatures.stream()
                    .filter(filter)
                    .filter(f -> f.supports(featureContext.getApplicationType()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.getBuildProperties().put("groovyVersion", VersionInfo.getDependencyVersion("groovy").getValue());
        }
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_GROOVY_RUNTIME);
    }

    @Override
    public boolean isGroovy() {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getLanguage() == Language.GROOVY;
    }
}
