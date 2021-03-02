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
package io.micronaut.starter.cli.command;

import io.micronaut.context.annotation.Prototype;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.ContextFactory;
import io.micronaut.starter.application.MessagingAvailableFeatures;
import io.micronaut.starter.application.generator.ProjectGenerator;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = CreateMessagingCommand.NAME, description = "Creates a messaging application")
@Prototype
public class CreateMessagingCommand extends CreateCommand {

    public static final String NAME = "create-messaging-app";

    @ReflectiveAccess
    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",", description = "The features to use. Possible values: ${COMPLETION-CANDIDATES}", completionCandidates = MessagingAvailableFeatures.class)
    List<String> features = new ArrayList<>();

    public CreateMessagingCommand(MessagingAvailableFeatures createMessagingFeatures,
                                  ContextFactory contextFactory,
                                  ProjectGenerator projectGenerator) {
        super(createMessagingFeatures, contextFactory, ApplicationType.MESSAGING, projectGenerator);
    }

    @NonNull
    @Override
    protected List<String> getSelectedFeatures() {
        return features;
    }
}
