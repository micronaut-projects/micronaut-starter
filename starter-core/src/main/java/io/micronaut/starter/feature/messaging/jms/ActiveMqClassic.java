/*
 * Copyright 2021 original authors
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
package io.micronaut.starter.feature.messaging.jms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;

import javax.inject.Singleton;

@Singleton
public class ActiveMqClassic extends AbstractJmsFeature {

    public static final String NAME = "jms-activemq-classic";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "ActiveMQ Classic JMS Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for ActiveMQ Classic JMS in the application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.jms.activemq.classic.enabled", true);
        generatorContext.getConfiguration().put("micronaut.jms.activemq.classic.connection-string", "tcp://localhost:61616");
    }
}
