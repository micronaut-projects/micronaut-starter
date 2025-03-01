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
package io.micronaut.starter.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.options.Options;

import jakarta.inject.Singleton;
import java.util.Set;

@Singleton
public class GrpcRandomPort implements DefaultFeature {

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.GRPC;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("grpc.server.port", SocketUtils.findAvailableTcpPort());
    }

    @NonNull
    @Override
    public String getName() {
        return "grpc-random-port";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
