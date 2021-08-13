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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.reactor.Reactor;
import io.micronaut.starter.feature.reactor.ReactorHttpClient;
import io.micronaut.starter.feature.rxjava.RxJavaThree;
import io.micronaut.starter.feature.rxjava.RxJavaThreeHttpClient;
import io.micronaut.starter.feature.rxjava.RxJavaTwo;
import io.micronaut.starter.feature.rxjava.RxJavaTwoHttpClient;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class HttpClient implements DefaultFeature {

    private final RxJavaTwoHttpClient rxJavaTwoHttpClient;
    private final RxJavaThreeHttpClient rxJavaThreeHttpClient;
    private final ReactorHttpClient reactorHttpClient;

    public HttpClient(RxJavaTwoHttpClient rxJavaTwoHttpClient,
                      RxJavaThreeHttpClient rxJavaThreeHttpClient,
                      ReactorHttpClient reactorHttpClient) {
        this.rxJavaTwoHttpClient = rxJavaTwoHttpClient;
        this.rxJavaThreeHttpClient = rxJavaThreeHttpClient;
        this.reactorHttpClient = reactorHttpClient;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT &&
                selectedFeatures.stream().noneMatch(feature -> feature instanceof FunctionFeature);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(RxJavaTwo.class)) {
            featureContext.addFeature(rxJavaTwoHttpClient);
        }
        if (featureContext.isPresent(RxJavaThree.class)) {
            featureContext.addFeature(rxJavaThreeHttpClient);
        }
        if (featureContext.isPresent(Reactor.class)) {
            featureContext.addFeature(reactorHttpClient);
        }
    }

    @Override
    public String getName() {
        return "http-client";
    }

    @Override
    public String getTitle() {
        return "Micronaut HTTP Client";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Micronaut HTTP client";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#httpClient";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut")
                .artifactId("micronaut-http-client")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut")
                .artifactId("micronaut-http-validation")
                .versionProperty("micronaut.version")
                .annotationProcessor());
    }
}
