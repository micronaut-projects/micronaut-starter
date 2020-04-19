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
package io.micronaut.starter.feature.picocli.test.kotlintest;

import io.micronaut.core.naming.NameUtils;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class PicocliKotlinTest implements Feature {

    @Override
    public String getName() {
        return "picocli-kotlintest";
    }

    @Override
    public String getTitle() {
        return NameUtils.camelCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("picocliKotlinTest", getTemplate(commandContext.getProject()));
    }

    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate("src/test/groovy/{packagePath}/{className}CommandSpec.kt", picocliKotlinTestTest.template(project));
    }

    @Override
    public boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_CLI_APP;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
