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
package io.micronaut.starter.feature.messaging.nats;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.messaging.MessagingFeature;

import jakarta.inject.Singleton;
import java.util.Collections;

@Singleton
public class Nats implements MessagingFeature {

    public static final String NAME = "nats";
    public static final Dependency MICRONAUT_NATS = Dependency.builder()
            .groupId("io.micronaut.nats")
            .artifactId("micronaut-nats")
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Nats.io Messaging";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Nats.io messaging";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-nats/snapshot/guide/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("nats.default.addresses", Collections.singletonList("nats://localhost:4222"));
        generatorContext.addDependency(MICRONAUT_NATS);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
