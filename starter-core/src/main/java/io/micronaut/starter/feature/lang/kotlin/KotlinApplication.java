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
package io.micronaut.starter.feature.lang.kotlin;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class KotlinApplication implements KotlinApplicationFeature {

    @Override
    public String mainClassName(Project project) {
        return project.getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "application";
    }

    @Override
    public boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_APP || command == MicronautCommand.CREATE_GRPC;
    }

    @Override
    public void apply(CommandContext commandContext) {
        KotlinApplicationFeature.super.apply(commandContext);

        commandContext.addTemplate("application", new RockerTemplate(getPath(),
                application.template(commandContext.getProject(), commandContext.getFeatures())));
    }

    protected String getPath() {
        return "src/main/kotlin/{packagePath}/Application.kt";
    }
}
