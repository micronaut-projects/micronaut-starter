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
package io.micronaut.starter.feature.grpc;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.grpc.template.proto;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Grpc implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, TestFramework testFramework, BuildTool buildTool, List<Feature> selectedFeatures) {
        return micronautCommand == MicronautCommand.CREATE_GRPC;
    }

    @Override
    public boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_GRPC;
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("proto", new RockerTemplate("src/main/proto/{propertyName}.proto", proto.template(commandContext.getProject())));
    }

    @Override
    public String getName() {
        return "grpc";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
