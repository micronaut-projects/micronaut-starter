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
package io.micronaut.starter.feature.stackdriver;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import io.micronaut.starter.feature.gcp.GcpFeature;
import jakarta.inject.Singleton;

@Singleton
public class CloudTrace extends GcpFeature implements Feature {
    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "gcp-cloud-trace";
    }

    @Override
    public String getTitle() {
        return "Google Cloud Trace";
    }

    @Override
    public String getDescription() {
        return "Adds support for distributed tracing Google Cloud Trace";
    }

    @Override
    public String getCategory() {
        return Category.TRACING;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://cloud.google.com/trace";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#tracing";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(MicronautDependencyUtils.gcpDependency()
                .artifactId("micronaut-gcp-tracing")
                .compile());
    }
}
