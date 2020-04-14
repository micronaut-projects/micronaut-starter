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
package io.micronaut.starter.feature.externalconfig;

import io.micronaut.starter.command.CommandContext;

import javax.inject.Singleton;

@Singleton
public class ConfigConsul implements ExternalConfigFeature {

    @Override
    public String getName() {
        return "config-consul";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Consul (https://www.consul.io)";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getBootstrapConfig().put("micronaut.application.name", commandContext.getProject().getName());
        commandContext.getBootstrapConfig().put("micronaut.config-client.enabled", true);
        commandContext.getBootstrapConfig().put("consul.client.registration.enabled", true);
        commandContext.getBootstrapConfig().put("consul.client.defaultZone", "${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}");
    }
}
