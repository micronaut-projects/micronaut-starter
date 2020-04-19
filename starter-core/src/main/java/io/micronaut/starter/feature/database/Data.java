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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;

@Singleton
public class Data implements Feature {

    @Override
    public String getName() {
        return "data";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.maven) {
            commandContext.getBuildProperties().put("micronaut.data.version", "1.0.2");
        }
    }
}
