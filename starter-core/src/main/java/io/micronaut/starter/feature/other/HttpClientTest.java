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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.features.validation.MicronautHttpValidation;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.httpclient.HttpClientFeature;
import io.micronaut.starter.feature.httpclient.HttpClientJdk;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;
import java.util.Set;

import static io.micronaut.starter.feature.httpclient.HttpClientJdk.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK;
import static io.micronaut.starter.feature.other.HttpClient.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT;

@Requires(property = "micronaut.starter.feature.http.client.test.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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

    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK_TEST = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK)
            .test()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK_COMPILE_ONLY = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK)
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
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(HttpClientFeature.class::isInstance);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!hasHttpClientFeatureDependencyInScope(generatorContext, Scope.COMPILE)) {
            if (generatorContext.getFeatures().hasFeature(AwsLambdaCustomRuntime.class) || (generatorContext.getFeatures().hasFeature(AwsLambda.class) && generatorContext.getFeatures().hasFeature(GraalVM.class))) {
                generatorContext.addDependency(HttpClientJdk.DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK);
            } else if (generatorContext.getOptions() instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.DEFAULT) {
                generatorContext.addDependency(generatorContext.getFeatures().hasFeature(AwsLambda.class)
                                ? DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK_TEST
                                : DEPENDENCY_MICRONAUT_HTTP_CLIENT_TEST);
                if (generatorContext.hasFeature(MicronautHttpValidation.class) && OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
                    generatorContext.addDependency(generatorContext.getFeatures().hasFeature(AwsLambda.class)
                            ? DEPENDENCY_MICRONAUT_HTTP_CLIENT_JDK_COMPILE_ONLY
                            : DEPENDENCY_MICRONAUT_HTTP_CLIENT_COMPILE_ONLY);
                }
            }
        }
    }

    private boolean hasHttpClientFeatureDependencyInScope(@NonNull GeneratorContext generatorContext, @NonNull Scope scope) {
        return generatorContext.hasDependencyInScope(MicronautDependencyUtils.GROUP_ID_MICRONAUT, ARTIFACT_ID_MICRONAUT_HTTP_CLIENT, scope) ||
                generatorContext.hasDependencyInScope(MicronautDependencyUtils.GROUP_ID_MICRONAUT, ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK, scope);
    }

    @Override
    public int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }
}
