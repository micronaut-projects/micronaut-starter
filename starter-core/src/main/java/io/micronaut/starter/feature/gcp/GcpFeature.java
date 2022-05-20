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
package io.micronaut.starter.feature.gcp;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.maven.Profile;
import io.micronaut.starter.feature.build.maven.Property;
import io.micronaut.starter.feature.graalvm.GraalVM;

public abstract class GcpFeature implements Feature {
    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().isFeaturePresent(GraalVM.class)) {
            if (generatorContext.getBuildTool().isGradle()) {
                generatorContext.addDependency(googleCloudNativeImageSupport().nativeImageCompileOnly());
            } else {
                generatorContext.addProfile(Profile.builder()
                        .id("graalVM")
                        .activationProperty(Property.builder().name("packaging").value("native-image").build())
                        .dependency(googleCloudNativeImageSupport().build())
                        .build());
            }
        }
    }

    @NonNull
    private Dependency.Builder googleCloudNativeImageSupport() {
        return Dependency.builder()
                .groupId("com.google.cloud")
                .artifactId("native-image-support");
    }
}
