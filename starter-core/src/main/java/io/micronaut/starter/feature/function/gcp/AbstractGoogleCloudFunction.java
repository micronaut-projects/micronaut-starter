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
package io.micronaut.starter.feature.function.gcp;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.other.ShadePlugin;

public abstract class AbstractGoogleCloudFunction extends AbstractFunctionFeature implements GcpCloudFeature, GcpMicronautRuntimeFeature {
    private static final Dependency DEPENDENCY_MICRONAUT_SERVLET_CORE = MicronautDependencyUtils.servletDependency()
                    .artifactId("micronaut-servlet-core")
                    .test()
                    .build();

    private final ShadePlugin shadePlugin;

    public AbstractGoogleCloudFunction(ShadePlugin shadePlugin) {
        this.shadePlugin = shadePlugin;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        if (generatorContext.getFeatures().testFramework().isSpock()) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_SERVLET_CORE);
        }
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(ShadePlugin.class)) {
            featureContext.addFeature(shadePlugin);
        }
    }
}
