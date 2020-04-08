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
package io.micronaut.starter.feature.logging;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.feature.logging.template.log4j2;

import javax.inject.Singleton;

@Singleton
public class Log4j2 implements LoggingFeature {

    @Override
    public String getName() {
        return "log4j2";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/log4j2.xml", log4j2.template(commandContext.getProject())));
    }
}
