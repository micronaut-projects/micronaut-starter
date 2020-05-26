/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.distributedconfig;

import io.micronaut.starter.application.generator.GeneratorContext;

import javax.inject.Singleton;

@Singleton
public class Consul implements DistributedConfigFeature {

    @Override
    public String getName() {
        return "config-consul";
    }

    @Override
    public String getTitle() {
        return "Consul Distributed Configuration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Consul";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getBootstrapConfig().put("micronaut.config-client.enabled", true);
        generatorContext.getBootstrapConfig().put("consul.client.registration.enabled", true);
        generatorContext.getBootstrapConfig().put("consul.client.defaultZone", "${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}");
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.consul.io";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#distributedConfigurationConsul";
    }
}
