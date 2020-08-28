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
package io.micronaut.starter.application.generator;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Options;

import java.util.List;

@DefaultImplementation(DefaultProjectGenerator.class)
public interface ProjectGenerator {

    void generate(ApplicationType applicationType,
                         Project project,
                         Options options,
                         @Nullable OperatingSystem operatingSystem,
                         List<String> selectedFeatures,
                         OutputHandler outputHandler,
                         ConsoleOutput consoleOutput) throws Exception;

    void generate(
            ApplicationType applicationType,
            Project project,
            OutputHandler outputHandler,
            GeneratorContext generatorContext) throws Exception;

    GeneratorContext createGeneratorContext(
            ApplicationType applicationType,
            Project project,
            Options options,
            @Nullable OperatingSystem operatingSystem,
            List<String> selectedFeatures,
            ConsoleOutput consoleOutput);
}
