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
package io.micronaut.starter.feature.messaging.pubsub;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.gcp.GcpFeature;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import jakarta.inject.Singleton;

@Singleton
public class PubSub extends GcpFeature implements MessagingFeature {

    public static final String NAME = "gcp-pubsub";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Google Cloud Pub/Sub";
    }

    @Override
    public String getDescription() {
        return "Adds support for Google Cloud Pub/Sub real-time messaging service";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://cloud.google.com/pubsub/docs";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/#pubsub";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(MicronautDependencyUtils.gcpDependency()
                .artifactId("micronaut-gcp-pubsub")
                .compile());
    }
}
