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
package io.micronaut.starter.cli.feature;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.diff.FeatureDiffer;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;

@Command(name = "feature-diff", description = "Produces the diff of an original project with an original project with additional features.")
public class FeatureDiffCommand extends CodeGenCommand {

    @ReflectiveAccess
    @Option(names = {"--features"}, paramLabel = "FEATURE", split = ",", description = "The additional features")
    List<String> features = new ArrayList<>();

    private final ProjectGenerator projectGenerator;
    private final FeatureDiffer featureDiffer;

    @Inject
    public FeatureDiffCommand(@Parameter CodeGenConfig config, ProjectGenerator projectGenerator, FeatureDiffer featureDiffer) {
        super(config);
        this.projectGenerator = projectGenerator;
        this.featureDiffer = featureDiffer;
    }

    @Override
    public boolean applies() {
        return true;
    }

    @Override
    public Integer call() throws Exception {
        String appName = FileSystemOutputHandler.getDefaultBaseDirectory().getName();
        Project project = NameUtils.parse(config.getDefaultPackage() + "." + appName);
        Options options = new Options(config.getSourceLanguage(), config.getTestFramework(), config.getBuildTool());
        ApplicationType applicationType = config.getApplicationType();
        List<String> features = this.features;
        ProjectGenerator projectGenerator = this.projectGenerator;
        featureDiffer.produceDiff(
                projectGenerator,
                project,
                applicationType,
                options,
                getOperatingSystem(),
                features,
                this
        );

        return 0;
    }
}
