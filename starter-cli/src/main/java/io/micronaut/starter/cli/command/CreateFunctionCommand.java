/*
 * Copyright 2017-2022 original authors
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
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.application.ContextFactory;
import io.micronaut.starter.application.FunctionAvailableFeatures;
import io.micronaut.starter.application.generator.ProjectGenerator;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.starter.application.ApplicationType.FUNCTION;

@Command(name = CreateFunctionCommand.NAME, description = "Creates a Cloud Function")
@Prototype
public class CreateFunctionCommand extends CreateCommand {

    public static final String NAME = "create-function-app";

    @ReflectiveAccess
    @Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",",
            description = "The features to use. Possible values: ${COMPLETION-CANDIDATES}",
            completionCandidates = FunctionAvailableFeatures.class)
    protected List<String> features = new ArrayList<>();

    public CreateFunctionCommand(FunctionAvailableFeatures availableFeatures,
                                 ContextFactory contextFactory,
                                 ProjectGenerator projectGenerator) {
        super(availableFeatures, contextFactory, FUNCTION, projectGenerator);
    }

    @NonNull
    @Override
    protected List<String> getSelectedFeatures() {
        return features;
    }
}
