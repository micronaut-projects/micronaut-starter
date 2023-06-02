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
package io.micronaut.starter.feature.consul;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;

import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
public class Consul implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "consul";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> config;
        if (generatorContext.isFeaturePresent(DistributedConfigFeature.class)) {
            config = generatorContext.getBootstrapConfiguration();
        } else {
            config = generatorContext.getConfiguration();
        }

        config.put("consul.client.defaultZone", "${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}");
    }
}
