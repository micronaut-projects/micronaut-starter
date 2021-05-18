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
package io.micronaut.starter.feature.stackdriver;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class CloudTrace implements Feature {
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
        return "Cloud Trace - Google Cloud Operations";
    }

    @Override
    public String getDescription() {
        return "Integrates Micronaut with Cloud Trace from Google Cloud Operations (formerly Stackdriver)";
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
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.gcp")
                .artifactId("micronaut-gcp-tracing")
                .compile());
    }
}
