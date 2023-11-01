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
package io.micronaut.starter.feature.other;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.httpclient.HttpClientFeature;
import io.micronaut.starter.feature.httpclient.HttpClientJdk;
import io.micronaut.starter.feature.validator.MicronautHttpValidation;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;
import java.util.Set;

import static io.micronaut.starter.feature.other.HttpClient.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT;

@Singleton
public class HttpClientTest implements DefaultFeature {

    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_TEST = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT)
            .test()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_COMPILE_ONLY = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT)
            .compileOnly()
            .build();

    @Override
    public String getName() {
        return "http-client-test";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(HttpClientFeature.class::isInstance);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!hasHttpClientFeatureDependencyInScope(generatorContext, Scope.COMPILE)) {
            if (generatorContext.getFeatures().hasFeature(AwsLambdaCustomRuntime.class) || (generatorContext.getFeatures().hasFeature(AwsLambda.class) && generatorContext.getFeatures().hasFeature(GraalVM.class))) {
                generatorContext.addDependency(HttpClient.DEPENDENCY_MICRONAUT_HTTP_CLIENT);
            } else if (generatorContext.getApplicationType() == ApplicationType.DEFAULT) {
                generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_CLIENT_TEST);
                if (generatorContext.hasFeature(MicronautHttpValidation.class) && generatorContext.getBuildTool().isGradle()) {
                    generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_CLIENT_COMPILE_ONLY);
                }
            }
        }
    }

    private boolean hasHttpClientFeatureDependencyInScope(@NonNull GeneratorContext generatorContext, @NonNull Scope scope) {
        return generatorContext.hasDependencyInScope(MicronautDependencyUtils.GROUP_ID_MICRONAUT, ARTIFACT_ID_MICRONAUT_HTTP_CLIENT, scope) ||
                generatorContext.hasDependencyInScope(MicronautDependencyUtils.GROUP_ID_MICRONAUT, HttpClientJdk.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK, scope);
    }

    @Override
    public int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }
}
