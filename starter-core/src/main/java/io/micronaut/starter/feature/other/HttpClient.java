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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.httpclient.HttpClientFeature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class HttpClient implements HttpClientFeature, DefaultFeature {

    public static final String NAME = "http-client";

    private static final Dependency.Builder DEPENDENCY_MICRONAUT_HTTP_CLIENT = MicronautDependencyUtils.coreDependency()
            .artifactId("micronaut-http-client");

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    private Scope dependencyScope(GeneratorContext generatorContext) {
        return (
                generatorContext.getFeatures().hasFeature(AwsLambdaCustomRuntime.class) ||
                (
                    generatorContext.getApplicationType() == ApplicationType.DEFAULT &&
                    generatorContext.getFeatures().getFeatures().stream().noneMatch(f -> f instanceof FunctionFeature)
                )
        ) ? Scope.COMPILE : Scope.TEST;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "HTTP Client";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Micronaut HTTP client";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#httpClient";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_CLIENT.scope(dependencyScope(generatorContext)));
        if (generatorContext.getApplicationType() ==  ApplicationType.DEFAULT) {
            generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                    .artifactId("micronaut-http-validation")
                    .versionProperty("micronaut.version")
                    .annotationProcessor());
        }
    }
}
