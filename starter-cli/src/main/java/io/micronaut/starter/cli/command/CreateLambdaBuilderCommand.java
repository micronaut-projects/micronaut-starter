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
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.AmazonApiGateway;
import io.micronaut.starter.feature.aws.AwsApiFeature;
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.aws.LambdaFunctionUrl;
import io.micronaut.starter.feature.aws.LambdaTrigger;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandLine.Command(name = CreateLambdaBuilderCommand.NAME, description = "A guided walk-through to create an lambda function")
@Prototype
public class CreateLambdaBuilderCommand extends BuilderCommand {

    public static final String NAME = "create-aws-lambda";

    private final ProjectGenerator projectGenerator;
    private final List<Feature> features;
    private final GraalVMFeatureValidator graalVMFeatureValidator;

    public CreateLambdaBuilderCommand(ProjectGenerator projectGenerator,
                                      List<Feature> features,
                                      GraalVMFeatureValidator graalVMFeatureValidator) {
        this.projectGenerator = projectGenerator;
        this.features = features;
        this.graalVMFeatureValidator = graalVMFeatureValidator;
    }

    @Override
    public Integer call() throws Exception {
        AnsiConsole.systemInstall();
        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(new DefaultParser())
                    .build();
            GenerateOptions options = createGenerateOptions(reader);
            Set<String> selectedFeatures = options.getFeatures();
            selectedFeatures.addAll(getFeatures(options.getApplicationType(), terminal, features));
            Project project = getProject(reader);
            try (OutputHandler outputHandler = new FileSystemOutputHandler(project, false, this)) {
                projectGenerator.generate(options.getApplicationType(),
                        project,
                        options.getOptions(),
                        getOperatingSystem(),
                        new ArrayList<>(selectedFeatures),
                        outputHandler,
                        this);
                out("@|blue ||@ Application created at " + outputHandler.getOutputLocation());
            }
        } catch (UserInterruptException | EndOfFileException e) {
            //no-op
        } finally {
            AnsiConsole.systemUninstall();
        }
        return 0;
    }

    public GenerateOptions createGenerateOptions(LineReader reader) {
        Set<String> applicationFeatures = new HashSet<>();
        applicationFeatures.add(AwsLambda.FEATURE_NAME_AWS_LAMBDA);
        CodingStyle codingStyle = getCodingStyle(reader);
        ApplicationType applicationType = applicationTypeForCodingStyle(codingStyle);
        if (codingStyle == CodingStyle.CONTROLLERS) {
            Feature apiFeature = getApiTrigger(applicationType, reader);
            applicationFeatures.add(apiFeature.getName());
        } else {
            Feature trigger = getTrigger(reader);
            applicationFeatures.add(trigger.getName());
        }

        LambdaDeployment deployment = getLambdaDeployment(reader);
        if (deployment == LambdaDeployment.NATIVE_EXECUTABLE) {
            applicationFeatures.add(GraalVM.FEATURE_NAME_GRAALVM);
        }

        getArchitecture(reader).ifPresent(architecture -> {
            if (architecture instanceof Arm) {
                applicationFeatures.add(Arm.NAME);
            } else if (architecture instanceof X86) {
                applicationFeatures.add(X86.NAME);
            }
        });

        getCdk(reader).ifPresent(f -> applicationFeatures.add(f.getName()));

        Language language = getLanguage(deployment, reader);
        TestFramework testFramework = getTestFramework(reader, language);
        BuildTool buildTool = getBuildTool(reader, language);
        JdkVersion jdkVersion = getJdkVersion(deployment, reader);
        Options options = new Options(language, testFramework, buildTool, jdkVersion);
        return new GenerateOptions(applicationType, options, applicationFeatures);
    }

    protected JdkVersion getJdkVersion(LambdaDeployment deployment, LineReader reader) {
        JdkVersion[] versions = jdkVersionsForDeployment(deployment);
        out("Choose the target JDK. (enter for default)");
        return getEnumOption(
                versions,
                jdkVersion -> Integer.toString(jdkVersion.majorVersion()),
                versions.length > 0 ? versions[0] : JdkVersion.JDK_17,
                reader
        );
    }

    JdkVersion[] jdkVersionsForDeployment(LambdaDeployment deployment) {
        switch (deployment) {
            case NATIVE_EXECUTABLE:
                return new JdkVersion[] {
                        JdkVersion.JDK_17
                };
            case FAT_JAR:
            default:
                List<JdkVersion> supportedJdks = AwsLambdaFeatureValidator.supportedJdks();
                JdkVersion[] arr = new JdkVersion[supportedJdks.size()];
                supportedJdks.toArray(arr);
                return arr;
        }
    }

    protected ApplicationType applicationTypeForCodingStyle(CodingStyle codingStyle) {
        switch (codingStyle) {
            case HANDLER:
                return ApplicationType.FUNCTION;
            case CONTROLLERS:
            default:
                return ApplicationType.DEFAULT;

        }
    }

    protected Language[] languagesForDeployment(LambdaDeployment deployment) {
        return deployment == LambdaDeployment.NATIVE_EXECUTABLE ?
                Stream.of(Language.values())
                        .filter(GraalVMFeatureValidator::supports)
                        .toArray(Language[]::new) :
                Language.values();
    }

    protected Feature getApiTrigger(ApplicationType applicationType, LineReader reader) {
        Feature defaultFeature = features.stream().filter(AmazonApiGateway.class::isInstance).findFirst()
                .orElseThrow(() -> new ConfigurationException("default feature " + LambdaFunctionUrl.NAME + " not found"));
        out("Choose your trigger. (enter for " + defaultFeature.getTitle() + ")");
        return getFeatureOption(
                apiTriggerFeatures(applicationType, features),
                Feature::getTitle,
                defaultFeature,
                reader);
    }

    protected Feature getTrigger(LineReader reader) {
        Feature defaultFeature = features.stream().filter(LambdaFunctionUrl.class::isInstance).findFirst()
                .orElseThrow(() -> new ConfigurationException("default feature " + LambdaFunctionUrl.NAME + " not found"));
        out("Choose your trigger. (enter for " + defaultFeature.getTitle() + ")");
        return getFeatureOption(
                triggerFeatures(features),
                Feature::getTitle,
                defaultFeature,
                reader);
    }

    protected Optional<Feature> getArchitecture(LineReader reader) {
        List<Feature> cpuArchitecturesFeatures = features.stream()
                .filter(CpuArchitecture.class::isInstance)
                .collect(Collectors.toList());
        String defaultCpuArchitecture = X86.NAME;
        out("Choose your Lambda Architecture. (enter for " + defaultCpuArchitecture + ")");
        String option = getListOption(
                cpuArchitecturesFeatures.stream()
                        .map(Feature::getName)
                        .sorted()
                        .collect(Collectors.toList()),
                o -> o,
                defaultCpuArchitecture,
                reader);
        return cpuArchitecturesFeatures
                .stream()
                .filter(f -> f.getName().equals(option))
                .findFirst();
    }

    protected Language getLanguage(LambdaDeployment deployment, LineReader reader) {
        out("Choose your preferred language. (enter for default)");
        return getEnumOption(
                languagesForDeployment(deployment),
                language -> StringUtils.capitalize(language.getName()),
                Language.DEFAULT_OPTION,
                reader);
    }

    protected LambdaDeployment getLambdaDeployment(LineReader reader) {
        out("How do you want to deploy?. (enter for Java runtime)");
        return getEnumOption(
                LambdaDeployment.class,
                LambdaDeployment::getDescription,
                LambdaDeployment.FAT_JAR,
                reader);
    }

    protected CodingStyle getCodingStyle(LineReader reader) {
        out("How do you want to write your application? (enter for Controllers)");
        return getEnumOption(
                CodingStyle.class,
                CodingStyle::getDescription,
                CodingStyle.CONTROLLERS,
                reader);
    }

    protected Optional<Feature> getCdk(LineReader reader) {
        out("Do you want to generate infrastructure as code with CDK? (enter for yes)");
        return getEnumOption(
                YesOrNo.class,
                yesOrNo -> StringUtils.capitalize(yesOrNo.toString()),
                YesOrNo.YES,
                reader) == YesOrNo.YES ?
                features.stream()
                        .filter(Cdk.class::isInstance)
                        .findFirst() : Optional.empty() ;
    }

    protected List<Feature> apiTriggerFeatures(ApplicationType applicationType, Collection<Feature> features) {
        return features.stream()
                .filter(AwsApiFeature.class::isInstance)
                .filter(f -> f.supports(applicationType))
                .sorted(Comparator.comparing(Feature::getTitle).reversed())
                .collect(Collectors.toList());
    }

    protected List<Feature> triggerFeatures(Collection<Feature> features) {
        return features.stream()
                .filter(LambdaTrigger.class::isInstance)
                .sorted((o1, o2) -> {
                    if (o1 instanceof AwsApiFeature && (o2 instanceof AwsApiFeature)) {
                        return o2.getTitle().compareTo(o1.getTitle());
                    }
                    if (o1 instanceof AwsApiFeature) {
                        return -1;
                    }
                    if (o2 instanceof AwsApiFeature) {
                        return 1;
                    }
                    return o1.getTitle().compareTo(o2.getTitle());
                })
                .collect(Collectors.toList());
    }
}
