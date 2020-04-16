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
package io.micronaut.starter.feature.cassandra;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Cassandra implements Feature {

    @Override
    public String getName() {
        return "cassandra";
    }

    @Override
    public String getDescription() {
        return "Adds support for Cassandra in the application";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("cassandra.default.clusterName", "\"myCluster\"");
        commandContext.getConfiguration().put("cassandra.default.contactPoint", "\"localhost\"");
        commandContext.getConfiguration().put("cassandra.default.port", 9042);
        commandContext.getConfiguration().put("cassandra.default.maxSchemaAgreementWaitSeconds", 20);
        commandContext.getConfiguration().put("cassandra.default.ssl", true);
    }

}
