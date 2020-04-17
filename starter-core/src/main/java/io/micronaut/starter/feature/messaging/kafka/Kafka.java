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
package io.micronaut.starter.feature.messaging.kafka;

import io.micronaut.starter.Options;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.messaging.Platform;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class Kafka implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Options options, List<Feature> selectedFeatures) {
        Optional<Platform> platform = options.get("platform", Platform.class);
        return micronautCommand == MicronautCommand.CREATE_MESSAGING && platform.isPresent() && platform.get() == Platform.kafka;
    }

    @Override
    public String getName() {
        return "kafka";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("kafka.bootstrap.servers", "localhost:9092");
    }
}
