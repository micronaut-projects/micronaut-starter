/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.gcp.GcpCloudFeature;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.Category.CLOUD;

@Singleton
public class GoogleLogging implements GcpCloudFeature, Feature {

    public static final String NAME = "gcp-logging";

    private static final Dependency GOOGLE_LOGGING_DEPENDENCY =
            MicronautDependencyUtils.gcpDependency()
                    .artifactId("google-cloud-logging-logback")
                    .compile()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Google Logging";
    }

    @Override
    public String getDescription() {
        return "Provides integration with Google Cloud Logging";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/#logging";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://cloud.google.com/logging";
    }

    @Override
    public String getCategory() {
        return CLOUD;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(GOOGLE_LOGGING_DEPENDENCY);
    }
}
