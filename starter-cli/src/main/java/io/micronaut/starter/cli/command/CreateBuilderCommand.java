/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.cli.command;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import picocli.CommandLine.Command;

import java.util.Collections;
import java.util.List;

@Command(name = CreateBuilderCommand.NAME, description = "A guided walk-through to create an application")
@Prototype
public class CreateBuilderCommand extends BuilderCommand {

    public static final String NAME = "create";

    public CreateBuilderCommand(ProjectGenerator projectGenerator,
                                List<Feature> features) {
        super(projectGenerator, features);
    }

    @Override
    public GenerateOptions createGenerateOptions(LineReader reader) {
        ApplicationType applicationType = getApplicationType(reader);
        Options options = getOptions(reader);
        return new GenerateOptions(applicationType, options, Collections.emptySet());
    }

    protected ApplicationType getApplicationType(LineReader reader) throws UserInterruptException, EndOfFileException {
        out("What type of application do you want to create? (enter for default)");
        return getEnumOption(
                ApplicationType.class,
                ApplicationType::getTitle,
                ApplicationType.DEFAULT_OPTION,
                reader);
    }
}
